package com.clau.controller;

import com.clau.annotation.Route;
import com.clau.config.server.ControllerConfig;
import com.clau.model.Usuario;
import com.clau.service.UsuarioService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UsuarioController extends ControllerConfig {

  private UsuarioService servce;

  public UsuarioController(){
    this.servce = new UsuarioService();
  }

  @Route(path = "/usuarios")
  public void listarUsuarios(HttpExchange exchange) throws IOException {
    List<Usuario> usuarios = servce.getUsuarios();
    enviar(exchange, usuarios);
  }

  @Route(path = "/usuarios/teste")
  public void testarUsuarios(HttpExchange exchange) throws IOException {
    String response = "Testando usuários!";
    enviar(exchange, response);
  }

}
