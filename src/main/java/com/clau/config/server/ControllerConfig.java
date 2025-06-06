package com.clau.config.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

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
}
