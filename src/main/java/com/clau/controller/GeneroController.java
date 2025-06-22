package com.clau.controller;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.config.server.ControllerConfig;
import com.clau.model.Genero;
import com.clau.service.GeneroService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

@GroupPrefix(value = "/generos")
public class GeneroController extends ControllerConfig {

  private GeneroService generoService;

  public GeneroController() {
    this.generoService = new GeneroService();
  }

  @Route(path = "")
  public void listarGeneros(HttpExchange exchange) throws IOException {
    List<Genero> generos = generoService.findAll();
    if (generos.isEmpty()) {
      enviar(exchange, "Nenhum gênero encontrado.", 204);
    } else {
      enviar(exchange, generos);
    }
  }
}
