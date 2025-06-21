package com.clau.dto.response;

import com.clau.model.Autor;
import com.clau.model.Livro;

import java.util.Objects;
import java.util.Optional;

public class LivroResponseDTO {

  private Long id;
  private String titulo;
  private String autor;
  private Integer anoPublicacao;
  private String genero;
  private String descricao;

  public LivroResponseDTO(Long id, String titulo, String autor, Integer anoPublicacao, String genero, String descricao) {
    this.id = id;
    this.titulo = titulo;
    this.autor = autor;
    this.anoPublicacao = anoPublicacao;
    this.genero = genero;
    this.descricao = descricao;
  }

  public LivroResponseDTO(Livro livro) {
    this.id = livro.getId();
    this.titulo = livro.getTitulo();
    this.autor = Optional.ofNullable(livro.getAutor()).map(Autor::getNome).orElse("-");
    this.anoPublicacao = livro.getAnoPublicacao();
    this.genero = livro.getGenero().getNome();
    this.descricao = livro.getDescricao();
  }

  public LivroResponseDTO() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public Integer getAnoPublicacao() {
    return anoPublicacao;
  }

  public void setAnoPublicacao(Integer anoPublicacao) {
    this.anoPublicacao = anoPublicacao;
  }

  public String getGenero() {
    return genero;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
}
