package com.clau.config.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.HashMap;
import java.util.Map;

public class Router {

  private static final Router instance = new Router();
  private final Map<String, HttpHandler> routes = new HashMap<>();

  private String API_PREFIX = "/library/api/v1";

  private Router(){

  }

  public static Router getInstance(){
    return instance;
  }

  public void addRoute(String path, HttpHandler handler){
    routes.put(path, handler);
  }

  public void applyRoutes(HttpServer server){
    for(Map.Entry<String, HttpHandler> entry : routes.entrySet()){
      server.createContext(API_PREFIX + entry.getKey(), entry.getValue());
    }
  }

  public String getApiPrefix() {
    return API_PREFIX;
  }
}
