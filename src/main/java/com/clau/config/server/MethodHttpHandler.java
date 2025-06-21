package com.clau.config.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      } else {
        throw new RuntimeException(cause);
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Não foi possível acessar o método handler.", e);
    }
  }
}
