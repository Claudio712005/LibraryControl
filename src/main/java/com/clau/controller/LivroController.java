package com.clau.controller;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.config.server.ControllerConfig;
import com.clau.service.LivroService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

@GroupPrefix(value = "/livros")
public class LivroController extends ControllerConfig {

  private LivroService service;

  public LivroController() {
    this.service = new LivroService();
  }

  @Route(path = "")
  public void listarLivros(HttpExchange exchange) throws IOException {
    enviar(exchange, service.findAll());
  }
}
