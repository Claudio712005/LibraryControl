package com.clau.controller;

import com.clau.annotation.GroupPrefix;
import com.clau.annotation.Route;
import com.clau.annotation.SecurityRoute;
import com.clau.config.server.ControllerConfig;
import com.clau.dto.request.AluguelLivroRequestDTO;
import com.clau.dto.response.AluguelAtrasoResponseDTO;
import com.clau.dto.response.AluguelLivroResponseDTO;
import com.clau.enums.HttpMethod;
import com.clau.enums.Role;
import com.clau.service.AluguelLivroService;
import com.sun.net.httpserver.HttpExchange;

import java.time.LocalDateTime;
import java.util.List;

@GroupPrefix(value = "/aluguel-livros")
public class AluguelLivroController extends ControllerConfig {

  private AluguelLivroService aluguelLivroService;

  public AluguelLivroController(){
    this.aluguelLivroService = new AluguelLivroService();
  }

  @Route(path = "", method = HttpMethod.POST)
  @SecurityRoute(roles = Role.CLIENTE)
  public void efetuarAluguel(HttpExchange exchange) throws Exception {
    AluguelLivroRequestDTO requestDTO = (AluguelLivroRequestDTO) this.getBody(exchange, AluguelLivroRequestDTO.class);

    aluguelLivroService.efetuarAluguel(requestDTO);
    this.enviar(exchange, "Livro alugado com sucesso", 200);
  }

  @Route(path = "/usuario/{idUsuario}", method = HttpMethod.GET)
  @SecurityRoute(roles = {Role.CLIENTE, Role.ADMIN})
  public void buscarAlugueisPorUsuario(HttpExchange exchange) throws Exception {
    Long idUsuario = Long.parseLong(this.getPathVariable("idUsuario", exchange));

    List<AluguelLivroResponseDTO> alugueis = aluguelLivroService.buscarAlugueisPorUsuario(idUsuario).stream().map(AluguelLivroResponseDTO::new).toList();

    if (alugueis.isEmpty()){
      this.enviar(exchange, null, 204);
      return;
    }

    this.enviar(exchange, alugueis, 200);
  }

  @Route(path = "/devolver/{idAluguel}", method = HttpMethod.PUT)
  @SecurityRoute(roles = Role.CLIENTE)
  public void devolverLivro(HttpExchange exchange) throws Exception {
    Long idAluguel = Long.parseLong(this.getPathVariable("idAluguel", exchange));

    aluguelLivroService.devolverLivro(idAluguel);
    this.enviar(exchange, "Livro devolvido com sucesso", 200);
  }

  @Route(path = "/{idAluguel}", method = HttpMethod.GET)
  @SecurityRoute(roles = {Role.CLIENTE, Role.ADMIN})
  public void buscarAluguelPorId(HttpExchange exchange) throws Exception {
    Long idAluguel = Long.parseLong(this.getPathVariable("idAluguel", exchange));

    AluguelLivroResponseDTO aluguel = new AluguelLivroResponseDTO(aluguelLivroService.findById(idAluguel));

    this.enviar(exchange, aluguel, 200);
  }

  @Route(path = "/{idAluguel}", method = HttpMethod.DELETE)
  @SecurityRoute(roles = Role.ADMIN)
  public void excluirAluguel(HttpExchange exchange) throws Exception {
    Long idAluguel = Long.parseLong(this.getPathVariable("idAluguel", exchange));

    aluguelLivroService.removerAluguel(idAluguel);
    this.enviar(exchange, "Aluguel excluído com sucesso", 204);
  }

  @Route(path = "/estender/{idAluguel}", method = HttpMethod.PUT)
  @SecurityRoute(roles = Role.ADMIN)
  public void estenderAluguel(HttpExchange exchange) throws Exception {
    Long idAluguel = Long.parseLong(this.getPathVariable("idAluguel", exchange));
    LocalDateTime novaDataDevolucao = LocalDateTime.parse(this.getParam("novaDataDevolucao", exchange).toString());

    aluguelLivroService.estenderAluguel(idAluguel, novaDataDevolucao);
    this.enviar(exchange, "Prazo de aluguel estendido com sucesso", 200);
  }

  @Route(path = "/atrasados", method = HttpMethod.GET)
  @SecurityRoute(roles = Role.ADMIN)
  public void buscarAlugueisAtrasados(HttpExchange exchange) throws Exception {
    List<AluguelAtrasoResponseDTO> alugueisAtrasados = aluguelLivroService.listarClientesComAtraso();

    if (alugueisAtrasados.isEmpty()) {
      this.enviar(exchange, null, 204);
      return;
    }

    this.enviar(exchange, alugueisAtrasados, 200);
  }
}
