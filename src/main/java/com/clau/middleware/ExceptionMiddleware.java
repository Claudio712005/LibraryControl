package com.clau.middleware;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.clau.exception.BadRequestException;
import com.clau.exception.ConflictException;
import com.clau.exception.NotFoundException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ExceptionMiddleware implements HttpHandler {

  private final HttpHandler next;

  public ExceptionMiddleware(HttpHandler next) {
    this.next = next;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      next.handle(exchange);
    } catch (NotFoundException e) {
      sendResponse(exchange, 404, e.getMessage());
    } catch (BadRequestException e) {
      sendResponse(exchange, 400, e.getMessage());
    } catch (ConflictException e) {
      sendResponse(exchange, 409, e.getMessage());
    } catch (TokenExpiredException e){
      sendResponse(exchange, 401, "Token expirado");
    } catch (Exception e) {
      e.printStackTrace();
      sendResponse(exchange, 500, "Erro interno do servidor");
    }
  }

  private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
    byte[] bytes = message.getBytes();
    exchange.getResponseHeaders().set("Content-Type", "text/plain");
    exchange.sendResponseHeaders(statusCode, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }
}
