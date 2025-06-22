package com.clau.dto.response;

import com.clau.model.Genero;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneroResponseDTO {

  private Long id;
  private String nome;
  private String descricao;
  private List<LivroResponseDTO> livros;

  public GeneroResponseDTO() {
  }

  public GeneroResponseDTO(Long id, String nome, String descricao, List<LivroResponseDTO> livros) {
    this.id = id;
    this.nome = nome;
    this.descricao = descricao;
    this.livros = livros;
  }

  public GeneroResponseDTO(Genero entity){
    this.id = entity.getId();
    this.nome = entity.getNome();
    this.descricao = entity.getDescricao();
    this.livros = Optional.ofNullable(entity.getLivros())
            .orElse(new ArrayList<>())
            .stream().map(LivroResponseDTO::new).toList();
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

  public List<LivroResponseDTO> getLivros() {
    return livros;
  }

  public void setLivros(List<LivroResponseDTO> livros) {
    this.livros = livros;
  }
}
