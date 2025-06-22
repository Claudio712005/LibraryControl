package com.clau.controller;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.annotation.SecurityRoute;
import com.clau.config.server.ControllerConfig;
import com.clau.dto.request.AutorRequestDTO;
import com.clau.dto.response.AutorResponseDTO;
import com.clau.enums.HttpMethod;
import com.clau.enums.Role;
import com.clau.service.AutorService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

@GroupPrefix(value = "/autores")
public class AutorController extends ControllerConfig {

  private AutorService autorService;

  public AutorController() {
    this.autorService = new AutorService();
  }

  @Route(path = "")
  public void listarAutores(HttpExchange exchange) throws IOException {
    var autores = autorService.findAll().stream().map(AutorResponseDTO::new).toList();
    if (autores.isEmpty()) {
      enviar(exchange, "Nenhum autor encontrado.", 204);
    } else {
      enviar(exchange, autores);
    }
  }

  @Route(path = "/{id}")
  public void buscarAutorPorId(HttpExchange exchange) throws IOException {
    Long id = Long.valueOf(this.getPathVariable("id", exchange));
    AutorResponseDTO autor = new AutorResponseDTO(autorService.findById(id));
    enviar(exchange, autor);
  }

  @Route(path = "", method = HttpMethod.POST)
  @SecurityRoute(roles = Role.ADMIN)
  public void cadastrarAutor(HttpExchange exchange) throws Exception {
    AutorRequestDTO requestDTO = (AutorRequestDTO) this.getBody(exchange, AutorRequestDTO.class);
    autorService.cadastrarAutor(requestDTO);
    enviar(exchange, "Autor cadastrado com sucesso.", 201);
  }

  @Route(path = "/{id}", method = HttpMethod.PUT)
  @SecurityRoute(roles = Role.ADMIN)
  public void atualizarAutor(HttpExchange exchange) throws Exception {
    Long id = Long.valueOf(this.getPathVariable("id", exchange));
    AutorRequestDTO requestDTO = (AutorRequestDTO) this.getBody(exchange, AutorRequestDTO.class);
    autorService.atualizarAutor(requestDTO, id);
    enviar(exchange, "Autor atualizado com sucesso.");
  }

  @Route(path = "/{id}", method = HttpMethod.DELETE)
  @SecurityRoute(roles = Role.ADMIN)
  public void deletarAutor(HttpExchange exchange) throws IOException {
    Long id = Long.valueOf(this.getPathVariable("id", exchange));
    autorService.delete(id);
    enviar(exchange, "Autor deletado com sucesso.");
  }
}
