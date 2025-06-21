package com.clau.middleware;

import com.clau.enums.Role;
import com.clau.util.JwtUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class AuthMiddleware implements HttpHandler {

  private final HttpHandler next;
  private JwtUtil jwtUtil;
  private List<Role> allowedRoles;

  public AuthMiddleware(HttpHandler next, Role[] allowedRoles) {
    this.next = next;
    this.jwtUtil = new JwtUtil();
    this.allowedRoles = List.of(allowedRoles);
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      sendUnauthorized(exchange);
      return;
    }

    String token = authHeader.substring(7);
    try {
      String subject = jwtUtil.validarToken(token);

      if(subject == null) {
        sendForbidden(exchange);
      } else {
        String roleName = jwtUtil.getRoleFromToken(token);

        Role userRole = Role.valueOf(roleName.toUpperCase());
        if (!allowedRoles.isEmpty() && !allowedRoles.contains(userRole)) {
          sendForbidden(exchange);
          return;
        }

        next.handle(exchange);
      }

    } catch (Exception e) {
      throw e;
    }
  }

  private void sendUnauthorized(HttpExchange exchange) throws IOException {
    String response = "401 Unauthorized";
    exchange.sendResponseHeaders(401, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

  private void sendForbidden(HttpExchange exchange) throws IOException {
    String response = "403 Forbidden";
    exchange.sendResponseHeaders(403, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}
