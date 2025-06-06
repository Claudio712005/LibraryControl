package com.clau.middleware;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class AuthMiddleware implements HttpHandler {
  private final HttpHandler next;

  public AuthMiddleware(HttpHandler next) {
    this.next = next;
  }


  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
  }
}
