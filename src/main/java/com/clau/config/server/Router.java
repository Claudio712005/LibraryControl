package com.clau.config.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.HashMap;
import java.util.Map;

public class Router {

  private static final Router instance = new Router();
  private final Map<String, Map<String, HttpHandler>> routes = new HashMap<>();

  private String API_PREFIX = "/library/api/v1";

  private Router(){

  }

  public static Router getInstance(){
    return instance;
  }

  public void addRoute(String path, String method, HttpHandler handler) {
    routes.putIfAbsent(path, new HashMap<>());
    routes.get(path).put(method, handler);
  }

  public void applyRoutes(HttpServer server) {
    for (Map.Entry<String, Map<String, HttpHandler>> entry : routes.entrySet()) {
      String path = entry.getKey();
      Map<String, HttpHandler> methodHandlers = entry.getValue();

      server.createContext(API_PREFIX + path, exchange -> {
        String requestMethod = exchange.getRequestMethod().toUpperCase();
        HttpHandler handler = methodHandlers.get(requestMethod);

        if (handler != null) {
          handler.handle(exchange);
        } else {
          String response = "Método HTTP não suportado para este endpoint";
          exchange.sendResponseHeaders(405, response.length());
          exchange.getResponseBody().write(response.getBytes());
          exchange.close();
        }
      });
    }
  }


  public String getApiPrefix() {
    return API_PREFIX;
  }
}
