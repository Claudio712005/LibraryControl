package com.clau.controller;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.annotation.SecurityRoute;
import com.clau.config.server.ControllerConfig;
import com.clau.dto.request.LivroRequestDTO;
import com.clau.dto.response.LivroResponseDTO;
import com.clau.enums.HttpMethod;
import com.clau.enums.Role;
import com.clau.exception.BadRequestException;
import com.clau.service.LivroService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Optional;

@GroupPrefix(value = "/livros")
public class LivroController extends ControllerConfig {

  private LivroService service;

  public LivroController() {
    this.service = new LivroService();
  }

  @Route(path = "")
  public void listarLivros(HttpExchange exchange) throws IOException {
    enviar(exchange, service.findAll().stream().map(LivroResponseDTO::new).toList());
  }

  @Route(path = "/{id}")
  public void buscarLivroPorId(HttpExchange exchange) throws IOException {
    Long id = Long.parseLong(Optional.ofNullable(getPathVariable("id", exchange))
            .orElseThrow(() -> new BadRequestException("ID do livro não informado.")));
    enviar(exchange, new LivroResponseDTO(service.findById(id)));
  }

  @Route(path = "", method = HttpMethod.POST)
  @SecurityRoute(roles = Role.ADMIN)
  public void cadastrarLivro(HttpExchange exchange) throws Exception {
    service.cadastrarLisvro((LivroRequestDTO) this.getBody(exchange, LivroRequestDTO.class));
    enviar(exchange, "Livro cadastrado com sucesso!", 201);
  }

  @Route(path = "/{id}", method = HttpMethod.PUT)
  @SecurityRoute(roles = Role.ADMIN)
  public void atualizarLivro(HttpExchange exchange) throws Exception {
    Long id = Long.parseLong(Optional.ofNullable(getPathVariable("id", exchange))
            .orElseThrow(() -> new BadRequestException("ID do livro não informado.")));
    service.atualizarLivro(id, (LivroRequestDTO) this.getBody(exchange, LivroRequestDTO.class));
    enviar(exchange, "Livro atualizado com sucesso!");
  }

  @Route(path = "/{id}", method = HttpMethod.DELETE)
  @SecurityRoute(roles = Role.ADMIN)
  public void excluirLivro(HttpExchange exchange) throws IOException {
    Long id = Long.parseLong(Optional.ofNullable(getPathVariable("id", exchange))
            .orElseThrow(() -> new BadRequestException("ID do livro não informado.")));
    service.excluirLivro(id);
    enviar(exchange, "Livro excluído com sucesso!");
  }
}
