package com.clau.dto.request;

import com.clau.annotation.NotBlank;

public class AutorRequestDTO {
  @NotBlank
  private String nome;

  public AutorRequestDTO() {
  }

  public AutorRequestDTO(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
