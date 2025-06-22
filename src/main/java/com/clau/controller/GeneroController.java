package com.clau.controller;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.annotation.SecurityRoute;
import com.clau.config.server.ControllerConfig;
import com.clau.dto.request.GeneroRequestDTO;
import com.clau.dto.response.GeneroResponseDTO;
import com.clau.enums.HttpMethod;
import com.clau.enums.Role;
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
    List<GeneroResponseDTO> generos = generoService.findAll().stream().map(GeneroResponseDTO::new).toList();
    if (generos.isEmpty()) {
      enviar(exchange, "Nenhum gênero encontrado.", 204);
    } else {
      enviar(exchange, generos);
    }
  }

  @Route(path = "/{id}")
  public void buscarGeneroPorId(HttpExchange exchange) throws IOException {
    Long id = Long.valueOf(this.getPathVariable("id", exchange));
    GeneroResponseDTO genero = new GeneroResponseDTO(generoService.findById(id));
    enviar(exchange, genero);
  }

  @Route(path = "", method = HttpMethod.POST)
  @SecurityRoute(roles = Role.ADMIN)
  public void cadastrarGenero(HttpExchange exchange) throws Exception {
    GeneroRequestDTO genero = (GeneroRequestDTO) this.getBody(exchange, GeneroRequestDTO.class);
    generoService.cadastrarGenero(genero);
    enviar(exchange, "Gênero cadastrado com sucesso.", 201);
  }

  @Route(path = "/{id}", method = HttpMethod.PUT)
  @SecurityRoute(roles = Role.ADMIN)
  public void atualizarGenero(HttpExchange exchange) throws Exception {
    Long id = Long.valueOf(this.getPathVariable("id", exchange));
    GeneroRequestDTO genero = (GeneroRequestDTO) this.getBody(exchange, GeneroRequestDTO.class);
    generoService.atualizarGenero(genero, id);
    enviar(exchange, "Gênero atualizado com sucesso.");
  }

  @Route(path = "/{id}", method = HttpMethod.DELETE)
  @SecurityRoute(roles = Role.ADMIN)
  public void removerGenero(HttpExchange exchange) throws IOException {
    Long id = Long.valueOf(this.getPathVariable("id", exchange));
    generoService.excluirGenero(id);
    enviar(exchange, "Gênero removido com sucesso.");
  }

}