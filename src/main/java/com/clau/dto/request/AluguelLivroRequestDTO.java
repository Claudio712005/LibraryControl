package com.clau.dto.request;

import com.clau.annotation.NotNull;
import com.clau.annotation.NotPast;
import com.clau.annotation.Size;
import com.clau.deserializer.LocalDateTimeDeserializer;
import com.clau.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

public class AluguelLivroRequestDTO {
  @NotNull(message = "O ID do livro é obrigatório.")
  private Long idLivro;

  @NotNull(message = "O ID do usuário é obrigatório.")
  private Long idUsuario;

  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @NotNull(message = "A data de devolução é obrigatória.")
  @NotPast
  private LocalDateTime dataDevolucao;

  @Size(max = 700, message = "O registro de observação deve ter no máximo 700 caracteres.")
  private String registroObservacao;

  public AluguelLivroRequestDTO() {
  }

  public AluguelLivroRequestDTO(Long idLivro, Long idUsuario, LocalDateTime dataDevolucao, String registroObservacao) {
    this.idLivro = idLivro;
    this.idUsuario = idUsuario;
    this.dataDevolucao = dataDevolucao;
    this.registroObservacao = registroObservacao;
  }

  public Long getIdLivro() {
    return idLivro;
  }

  public void setIdLivro(Long idLivro) {
    this.idLivro = idLivro;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public LocalDateTime getDataDevolucao() {
    return dataDevolucao;
  }

  public void setDataDevolucao(LocalDateTime dataDevolucao) {
    this.dataDevolucao = dataDevolucao;
  }

  public String getRegistroObservacao() {
    return registroObservacao;
  }

  public void setRegistroObservacao(String registroObservacao) {
    this.registroObservacao = registroObservacao;
  }
}
