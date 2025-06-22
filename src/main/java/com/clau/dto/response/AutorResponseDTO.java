package com.clau.dto.response;

import com.clau.model.Autor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AutorResponseDTO {
  private Long id;
  private String nome;
  private List<LivroResponseDTO> livros;

  public AutorResponseDTO(Long id, String nome, List<LivroResponseDTO> livros) {
    this.id = id;
    this.nome = nome;
    this.livros = livros;
  }

  public AutorResponseDTO() {
  }

  public AutorResponseDTO(Autor autor) {
    this.id = autor.getId();
    this.nome = autor.getNome();
    this.livros = Optional.ofNullable(autor.getLivros())
            .map(livros -> livros.stream()
                    .map(LivroResponseDTO::new)
                    .toList())
            .orElse(new ArrayList<>());
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

  public List<LivroResponseDTO> getLivros() {
    return livros;
  }

  public void setLivros(List<LivroResponseDTO> livros) {
    this.livros = livros;
  }
}
