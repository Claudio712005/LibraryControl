package com.clau.model;

import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.Table;

@Table(name = "autores")
public class Autor {

  @Id
  @Column(name="id")
  private Long id;

  @Column(name="nome")
  private String nome;

  public Autor() {
  }

  public Autor(Long id, String nome) {
    this.id = id;
    this.nome = nome;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
