package com.clau.dto.response;

import com.clau.model.AluguelLivro;
import com.clau.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public class AluguelAtrasoResponseDTO {

  private Long idUsuario;
  private String nomeUsuario;
  private String emailUsuario;

  private List<AluguelSimpleResponseDTO> alugueisAtrasados;

  public AluguelAtrasoResponseDTO(Long idUsuario, String nomeUsuario, String emailUsuario, List<AluguelSimpleResponseDTO> alugueisAtrasados) {
    this.idUsuario = idUsuario;
    this.nomeUsuario = nomeUsuario;
    this.emailUsuario = emailUsuario;
    this.alugueisAtrasados = alugueisAtrasados;
  }

  public AluguelAtrasoResponseDTO() {
  }

  public AluguelAtrasoResponseDTO(Usuario usuario, List<AluguelLivro> aluguelLivros) {
    this.idUsuario = usuario.getId();
    this.nomeUsuario = usuario.getNome();
    this.emailUsuario = usuario.getEmail();

    this.alugueisAtrasados = aluguelLivros.stream().map(AluguelSimpleResponseDTO::new).toList();
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
  }

  public String getEmailUsuario() {
    return emailUsuario;
  }

  public void setEmailUsuario(String emailUsuario) {
    this.emailUsuario = emailUsuario;
  }

  public List<AluguelSimpleResponseDTO> getAlugueisAtrasados() {
    return alugueisAtrasados;
  }

  public void setAlugueisAtrasados(List<AluguelSimpleResponseDTO> alugueisAtrasados) {
    this.alugueisAtrasados = alugueisAtrasados;
  }

}

