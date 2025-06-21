package com.clau.controller;

import com.clau.enums.HttpMethod;
import com.clau.enums.Role;
import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.annotation.SecurityRoute;
import com.clau.config.server.ControllerConfig;
import com.clau.dto.request.UsuarioRequestDTO;
import com.clau.model.Usuario;
import com.clau.service.UsuarioService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

@GroupPrefix(value = "/usuarios")
public class UsuarioController extends ControllerConfig {

  private UsuarioService service;

  public UsuarioController() {
    this.service = new UsuarioService();
  }

  @Route(path = "/auth")
  public void login(HttpExchange exchange) throws IOException {
    String email = (String) getParam("email", exchange);
    String senha = (String) getParam("senha", exchange);

    enviar(exchange, service.login(email, senha));
  }

  @Route(path = "")
  public void listarUsuarios(HttpExchange exchange) throws IOException {
    List<Usuario> usuarios = service.getUsuarios();
    enviar(exchange, usuarios);
  }

  @Route(path = "", method = HttpMethod.POST)
  @SecurityRoute(roles = {Role.ADMIN})
  public void cadastrarUsuario(HttpExchange exchange) throws Exception {
    UsuarioRequestDTO usuario = (UsuarioRequestDTO) this.getBody(exchange, UsuarioRequestDTO.class);

    service.cadastrar(usuario);
    enviar(exchange, "Usuário cadastrado com sucesso!", 201);
  }

}
