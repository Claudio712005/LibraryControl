package com.clau.config.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class MethodHttpHandler implements HttpHandler {

  private final Object controller;
  private final Method method;

  public MethodHttpHandler(Object controller, Method method) {
    this.controller = controller;
    this.method = method;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      if (method.getParameterCount() == 1 &&
              method.getParameterTypes()[0].equals(HttpExchange.class)) {
        method.invoke(controller, exchange);
      } else {
        throw new IllegalArgumentException("Método handler precisa receber um HttpExchange como parâmetro.");
      }
    } catch (Exception e) {
      String error = "Erro interno: " + e.getMessage();
      e.printStackTrace();

      exchange.sendResponseHeaders(500, error.length());
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(error.getBytes());
      }
    }
  }
}
