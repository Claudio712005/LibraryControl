package com.clau.enums;

public enum Situacao {

  ALUGADO("Alugado"),
  DEVOLVIDO("Devolvido"),
  ATRASADO("Atrasado");

  private final String nome;

  Situacao(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return nome;
  }

  public static Situacao fromNome(String nome) {
    for (Situacao situacao : Situacao.values()) {
      if (situacao.getNome().equalsIgnoreCase(nome)) {
        return situacao;
      }
    }
    throw new IllegalArgumentException("Situacao do usuário não foi encontrada com o nome: " + nome);
  }

}
