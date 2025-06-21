package com.clau.dto.request;

import com.clau.annotation.NotBlank;
import com.clau.annotation.NotNull;
import com.clau.annotation.Size;
import com.clau.enums.Role;

public class UsuarioRequestDTO {

  @NotBlank(message = "O campo nome é obrigatório.")
  private String nome;
  @NotBlank(message = "O campo email é obrigatório.")
  private String email;
  @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres.")
  private String senha;
  @NotBlank(message = "O campo confirmação de senha é obrigatório.")
  private String confirmacaoSenha;
  @NotNull(message = "O campo role é obrigatório.")
  private Role role;

  public UsuarioRequestDTO() {
  }

  public UsuarioRequestDTO(String nome, String email, String senha, String confirmacaoSenha, Role role) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.confirmacaoSenha = confirmacaoSenha;
    this.role = role;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
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

  public String getConfirmacaoSenha() {
    return confirmacaoSenha;
  }

  public void setConfirmacaoSenha(String confirmacaoSenha) {
    this.confirmacaoSenha = confirmacaoSenha;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
