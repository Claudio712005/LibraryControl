package com.clau.dto.response;

import com.clau.model.AluguelLivro;

import java.time.LocalDateTime;

public class AluguelSimpleResponseDTO {
  private Long id;
  private String tituloLivro;
  private LocalDateTime dataDevolucao;
  private LocalDateTime dataAluguel;

  public AluguelSimpleResponseDTO(Long id, String tituloLivro, LocalDateTime dataDevolucao, LocalDateTime dataAluguel) {
    this.id = id;
    this.tituloLivro = tituloLivro;
    this.dataDevolucao = dataDevolucao;
    this.dataAluguel = dataAluguel;
  }

  public AluguelSimpleResponseDTO(AluguelLivro aluguelLivro){
    this.id = aluguelLivro.getId();
    this.tituloLivro = aluguelLivro.getLivro().getTitulo();
    this.dataDevolucao = aluguelLivro.getDataHoraDevolucao();
    this.dataAluguel = aluguelLivro.getDataHoraAluguel();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTituloLivro() {
    return tituloLivro;
  }

  public void setTituloLivro(String tituloLivro) {
    this.tituloLivro = tituloLivro;
  }

  public LocalDateTime getDataDevolucao() {
    return dataDevolucao;
  }

  public void setDataDevolucao(LocalDateTime dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public LocalDateTime getDataAluguel() {
    return dataAluguel;
  }

  public void setDataAluguel(LocalDateTime dataAluguel) {
    this.dataAluguel = dataAluguel;
  }
}
