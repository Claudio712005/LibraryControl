package com.clau.enums;

public enum Role {
  ADMIN("ADMIN", 1),
  CLIENTE("CLIENTE", 2),
  FUNCIONARIO("FUNCIONARIO", 3);

  private String nome;
  private int id;

  Role(String nome, int id) {
    this.nome = nome;
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public int getId() {
    return id;
  }

  public static Role fromId(int id) {
    for (Role role : Role.values()) {
      if (role.getId() == id) {
        return role;
      }
    }
    throw new IllegalArgumentException("Role do usuário não foi encontrada com o id: " + id);
  }

  public static Role fromNome(String nome) {
    for (Role role : Role.values()) {
      if (role.getNome().equalsIgnoreCase(nome)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Role do usuário não foi encontrada com o nome: " + nome);
  }
}
