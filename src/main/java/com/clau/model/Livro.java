package com.clau.model;

import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.ManyToOne;
import com.clau.annotation.Table;
import com.clau.dto.request.LivroRequestDTO;

@Table(name = "livros")
public class Livro {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "titulo")
  private String titulo;

  @ManyToOne(nameColumn = "autor_id", foreignKey = "id")
  private Autor autor;

  @Column(name = "ano_publicacao")
  private Integer anoPublicacao;

  @ManyToOne(nameColumn = "genero_id", foreignKey = "id")
  private Genero genero;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "disponivel")
  private Boolean disponivel;

  public Livro() {
  }

  public Livro(Long id, String titulo, Autor autor, Integer anoPublicacao, Genero genero, String descricao, Boolean disponivel) {
    this.id = id;
    this.titulo = titulo;
    this.autor = autor;
    this.anoPublicacao = anoPublicacao;
    this.genero = genero;
    this.descricao = descricao;
    this.disponivel = disponivel;
  }

  public Livro(LivroRequestDTO requestDTO) {
    this.titulo = requestDTO.getTitulo();
    this.autor = new Autor(requestDTO.getIdAutor());
    this.anoPublicacao = requestDTO.getAnoPublicacao();
    this.genero = new Genero(requestDTO.getIdGenero());
    this.descricao = requestDTO.getDescricao();
    this.disponivel = true;
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

  public Autor getAutor() {
    return autor;
  }

  public void setAutor(Autor autor) {
    this.autor = autor;
  }

  public Integer getAnoPublicacao() {
    return anoPublicacao;
  }

  public void setAnoPublicacao(Integer anoPublicacao) {
    this.anoPublicacao = anoPublicacao;
  }

  public Genero getGenero() {
    return genero;
  }

  public void setGenero(Genero genero) {
    this.genero = genero;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Boolean getDisponivel() {
    return disponivel;
  }

  public void setDisponivel(Boolean disponivel) {
    this.disponivel = disponivel;
  }
}
