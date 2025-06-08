package com.clau.dto.request;

public class LoginRequestDTO {

  private String email;
  private String senha;

  public LoginRequestDTO(String email, String senha) {
    this.email = email;
    this.senha = senha;
  }

  public LoginRequestDTO() {
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
