package com.clau.dto.response;

public class LoginResponseDTO {

  private String token;
  private UsuarioResponseDTO usuario;

  public LoginResponseDTO(String token, UsuarioResponseDTO usuario) {
    this.token = token;
    this.usuario = usuario;
  }

  public LoginResponseDTO() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UsuarioResponseDTO getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioResponseDTO usuario) {
    this.usuario = usuario;
  }
}
