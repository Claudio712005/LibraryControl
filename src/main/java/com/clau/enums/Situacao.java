package com.clau.enums;

public enum Situacao {

  ALUGADO("Alugado"),
  DEVOLVIDO("Devolvido"),
  ATRASADO("Atrasado");

  private final String descricao;

  Situacao(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }

}
