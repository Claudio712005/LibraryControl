package com.clau.dto.request;

import com.clau.annotation.NotBlank;
import com.clau.annotation.NotNull;

public class LivroRequestDTO {

  @NotBlank(message = "O título do livro não pode ser vazio.")
  private String titulo;
  @NotNull(message = "O autor do livro não pode ser nulo.")
  private Long idAutor;
  @NotNull(message = "O Gênero do livro não pode ser nulo.")
  private Long idGenero;
  @NotNull(message = "O ano de publicação do livro não pode ser nulo.")
  private Integer anoPublicacao;
  @NotBlank(message = "A descrição do livro não pode ser vazia.")
  private String descricao;

  public LivroRequestDTO(String titulo, Long idAutor, Long idGenero, Integer anoPublicacao, String descricao) {
    this.titulo = titulo;
    this.idAutor = idAutor;
    this.idGenero = idGenero;
    this.anoPublicacao = anoPublicacao;
    this.descricao = descricao;
  }

  public LivroRequestDTO() {
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Long getIdAutor() {
    return idAutor;
  }

  public void setIdAutor(Long idAutor) {
    this.idAutor = idAutor;
  }

  public Long getIdGenero() {
    return idGenero;
  }

  public void setIdGenero(Long idGenero) {
    this.idGenero = idGenero;
  }

  public Integer getAnoPublicacao() {
    return anoPublicacao;
  }

  public void setAnoPublicacao(Integer anoPublicacao) {
    this.anoPublicacao = anoPublicacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
}
