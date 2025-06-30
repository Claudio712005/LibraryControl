package com.clau.dto.response;

import com.clau.enums.Situacao;
import com.clau.model.AluguelLivro;

import java.time.LocalDateTime;
import java.util.Optional;

public class AluguelLivroResponseDTO {

  private Long id;

  private LivroResponseDTO livro;

  private UsuarioResponseDTO cliente;

  private LocalDateTime dataAluguel;

  private LocalDateTime dataDevolucao;

  private String situacao;

  private String registroObservacao;

  public AluguelLivroResponseDTO(){

  }

  public AluguelLivroResponseDTO(Long id, LivroResponseDTO livro, UsuarioResponseDTO cliente, LocalDateTime dataAluguel, LocalDateTime dataDevolucao, String situacao, String registroObservacao) {
    this.id = id;
    this.livro = livro;
    this.cliente = cliente;
    this.dataAluguel = dataAluguel;
    this.dataDevolucao = dataDevolucao;
    this.situacao = situacao;
    this.registroObservacao = registroObservacao;
  }

  public AluguelLivroResponseDTO(AluguelLivro aluguel){
    this.id = aluguel.getId();
    this.livro = new LivroResponseDTO(aluguel.getLivro());
    this.cliente = new UsuarioResponseDTO(aluguel.getCliente());
    this.dataAluguel = aluguel.getDataHoraAluguel();
    this.registroObservacao = aluguel.getRegistroObservacao();
    this.dataDevolucao = aluguel.getDataHoraDevolucao();
    this.situacao = Optional.ofNullable(aluguel.getSituacao()).map(Situacao::getNome).orElse("N/A");
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LivroResponseDTO getLivro() {
    return livro;
  }

  public void setLivro(LivroResponseDTO livro) {
    this.livro = livro;
  }

  public UsuarioResponseDTO getCliente() {
    return cliente;
  }

  public void setCliente(UsuarioResponseDTO cliente) {
    this.cliente = cliente;
  }

  public LocalDateTime getDataAluguel() {
    return dataAluguel;
  }

  public void setDataAluguel(LocalDateTime dataAluguel) {
    this.dataAluguel = dataAluguel;
  }

  public LocalDateTime getDataDevolucao() {
    return dataDevolucao;
  }

  public void setDataDevolucao(LocalDateTime dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public String getRegistroObservacao() {
    return registroObservacao;
  }

  public void setRegistroObservacao(String registroObservacao) {
    this.registroObservacao = registroObservacao;
  }
}
