package com.clau.config.server;

import com.clau.util.ValidatorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class ControllerConfig {

  private static final ObjectMapper mapper = new ObjectMapper();

  protected void enviar(HttpExchange exchange, Object response) throws IOException {
    String json = mapper.writeValueAsString(response);

    byte[] responseBytes = json.getBytes("UTF-8");
    exchange.sendResponseHeaders(200, responseBytes.length);

    try (OutputStream os = exchange.getResponseBody()) {
      os.write(responseBytes);
    }
  }

  protected void enviar(HttpExchange exchange, Object response, int statusCode) throws IOException {
    String json = mapper.writeValueAsString(response);

    byte[] responseBytes = json.getBytes("UTF-8");
    exchange.sendResponseHeaders(statusCode, responseBytes.length);

    try (OutputStream os = exchange.getResponseBody()) {
      os.write(responseBytes);
    }
  }

  protected Object getParam(String param, HttpExchange exchange) {
    URI query = exchange.getRequestURI();

    String queryString = query.getQuery();

    if (queryString != null && !queryString.isEmpty()) {
      String[] params = queryString.split("&");
      for (String p : params) {
        String[] keyValue = p.split("=");
        if (keyValue.length == 2 && keyValue[0].equals(param)) {
          return keyValue[1];
        }
      }
    }

    return null;
  }

  protected String getPathVariable(String variable, HttpExchange exchange) {
    String path = exchange.getRequestURI().getPath();
    String[] segments = path.split("/");

    return segments.length > 0 ? segments[segments.length - 1] : null;
  }

  protected Object getBody(HttpExchange exchange, Class<?> clazz) throws Exception {

    if (exchange.getRequestBody() == null) {
      return null;
    }

    Object body = mapper.readValue(exchange.getRequestBody(), clazz);
    new ValidatorUtil().validateObject(body);

    return body;
  }
}
