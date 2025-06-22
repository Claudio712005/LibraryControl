package com.clau.dto.request;

import com.clau.annotation.NotBlank;
import com.clau.annotation.Size;

public class GeneroRequestDTO {

  @NotBlank(message = "O nome do gênero é obrigatório")
  @Size(min = 0, max = 100, message = "O nome do gênero deve ter entre 0 e 100 caracteres")
  private String nome;

  @NotBlank(message = "A descrição do gênero é obrigatória")
  @Size(min = 0, max = 500, message = "A descrição do gênero deve ter entre 0 e 255 caracteres")
  private String descricao;

  public GeneroRequestDTO() {
  }

  public GeneroRequestDTO(String nome, String descricao) {
    this.nome = nome;
    this.descricao = descricao;
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
}
