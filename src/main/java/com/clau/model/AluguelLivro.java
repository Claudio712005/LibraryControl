package com.clau.model;

import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.ManyToOne;
import com.clau.annotation.Table;
import com.clau.enums.Situacao;

import java.time.LocalDateTime;

@Table(name = "aluguel_livro")
public class AluguelLivro {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "data_hora_aluguel")
  private LocalDateTime dataHoraAluguel;

  @Column(name = "registro_observacao")
  private String registroObservacao;

  @Column(name = "data_hora_devolucao")
  private LocalDateTime dataHoraDevolucao;

  @Column(name = "situacao")
  private Situacao situacao;

  @ManyToOne(nameColumn = "livro_id", foreignKey = "id", fetch = true)
  private Livro livro;

  @ManyToOne(nameColumn = "cliente_id", foreignKey = "id", fetch = true)
  private Usuario cliente;

  public AluguelLivro() {
  }

  public AluguelLivro(Long id, LocalDateTime dataHoraAluguel, String registroObservacao, LocalDateTime dataHoraDevolucao, Situacao situacao, Livro livro, Usuario cliente) {
    this.id = id;
    this.dataHoraAluguel = dataHoraAluguel;
    this.registroObservacao = registroObservacao;
    this.dataHoraDevolucao = dataHoraDevolucao;
    this.situacao = situacao;
    this.livro = livro;
    this.cliente = cliente;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getDataHoraAluguel() {
    return dataHoraAluguel;
  }

  public void setDataHoraAluguel(LocalDateTime dataHoraAluguel) {
    this.dataHoraAluguel = dataHoraAluguel;
  }

  public String getRegistroObservacao() {
    return registroObservacao;
  }

  public void setRegistroObservacao(String registroObservacao) {
    this.registroObservacao = registroObservacao;
  }

  public LocalDateTime getDataHoraDevolucao() {
    return dataHoraDevolucao;
  }

  public void setDataHoraDevolucao(LocalDateTime dataHoraDevolucao) {
    this.dataHoraDevolucao = dataHoraDevolucao;
  }

  public Situacao getSituacao() {
    return situacao;
  }

  public void setSituacao(Situacao situacao) {
    this.situacao = situacao;
  }

  public Livro getLivro() {
    return livro;
  }

  public void setLivro(Livro livro) {
    this.livro = livro;
  }

  public Usuario getCliente() {
    return cliente;
  }

  public void setCliente(Usuario cliente) {
    this.cliente = cliente;
  }
}
