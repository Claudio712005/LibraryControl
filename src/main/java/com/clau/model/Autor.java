package com.clau.model;

import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.OneToMany;
import com.clau.annotation.Table;

import java.util.List;

@Table(name = "autores")
public class Autor {

  @Id
  @Column(name="id")
  private Long id;

  @Column(name="nome")
  private String nome;

  @OneToMany(mappedBy = "autor_id", fetch = true, fkColumn = "autor_id")
  private List<Livro> livros;

  public Autor() {
  }

  public Autor(Long id) {
    this.id = id;
  }

  public Autor(Long id, String nome, List<Livro> livros) {
    this.id = id;
    this.nome = nome;
    this.livros = livros;
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

  public List<Livro> getLivros() {
    return livros;
  }

  public void setLivros(List<Livro> livros) {
    this.livros = livros;
  }
}
