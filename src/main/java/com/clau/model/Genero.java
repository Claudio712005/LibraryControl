package com.clau.model;

import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.OneToMany;
import com.clau.annotation.Table;
import com.clau.dto.request.GeneroRequestDTO;

import java.util.List;

@Table(name = "generos")
public class Genero {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "nome")
  private String nome;

  @Column(name = "descricao")
  private String descricao;

  @OneToMany(mappedBy = "genero_id", fkColumn = "genero_id", fetch = true)
  private List<Livro> livros;

  public Genero() {
  }

  public Genero(Long id, String nome, String descricao) {
    this.id = id;
    this.nome = nome;
    this.descricao = descricao;
  }

  public Genero(Long id){
    this.id = id;
  }

  public Genero(GeneroRequestDTO requestDTO) {
    this.nome = requestDTO.getNome();
    this.descricao = requestDTO.getDescricao();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public List<Livro> getLivros() {
    return livros;
  }

  public void setLivros(List<Livro> livros) {
    this.livros = livros;
  }
}
