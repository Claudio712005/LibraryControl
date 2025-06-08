package com.clau.middleware;

import com.clau.util.JwtUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class AuthMiddleware implements HttpHandler {
  private final HttpHandler next;
  private JwtUtil jwtUtil;

  public AuthMiddleware(HttpHandler next) {
    this.next = next;
    this.jwtUtil = new JwtUtil();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

    if(authHeader == null || !authHeader.startsWith("Bearer ")) {
      exchange.sendResponseHeaders(401, -1);
      return;
    }

    String token = authHeader.substring(7);
    try {
      String subject = jwtUtil.validarToken(token);

      if(subject == null) {
        sendForbidden(exchange);
      } else {
        next.handle(exchange);
      }

    } catch (Exception e) {
      sendUnauthorized(exchange);
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
