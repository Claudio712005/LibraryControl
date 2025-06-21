package com.clau.config.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    server.createContext(API_PREFIX, exchange -> {
      String path = exchange.getRequestURI().getPath()
              .substring(API_PREFIX.length());
      String method = exchange.getRequestMethod().toUpperCase();

      for (String template : routes.keySet()) {
        Pattern pattern = toRegex(template);
        Matcher m = pattern.matcher(path);
        if (m.matches()) {
          HttpHandler handler = routes.get(template).get(method);
          if (handler != null) {
            Map<String,String> pathVars = extractVariables(template, m);
            for (var e : pathVars.entrySet()) {
              exchange.setAttribute(e.getKey(), e.getValue());
            }
            handler.handle(exchange);
            return;
          }
        }
      }
      String resp = "Endereço ou método HTTP não suportado";
      exchange.sendResponseHeaders(404, resp.length());
      exchange.getResponseBody().write(resp.getBytes());
      exchange.close();
    });
  }

  Pattern toRegex(String template) {
    String regex = template.replaceAll("\\{([^/]+)\\}", "(?<$1>[^/]+)");
    return Pattern.compile("^" + regex + "$");
  }

  Map<String,String> extractVariables(String template, Matcher m) {
    Map<String,String> vars = new HashMap<>();
    Pattern p = Pattern.compile("\\{([^/]+)\\}");
    Matcher mm = p.matcher(template);
    while (mm.find()) {
      String var = mm.group(1);
      vars.put(var, m.group(var));
    }
    return vars;
  }

  public String getApiPrefix() {
    return API_PREFIX;
  }
}
