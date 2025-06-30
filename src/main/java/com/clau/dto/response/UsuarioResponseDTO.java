package com.clau.dto.response;

import com.clau.enums.Role;
import com.clau.model.Usuario;

import java.util.Optional;

public class UsuarioResponseDTO {

  private Long id;
  private String nome;
  private String email;
  private String role;

  public UsuarioResponseDTO(Long id, String nome, String email, String role) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.role = role;
  }

  public UsuarioResponseDTO(Long id, String nome, String email, Role role) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.role = role != null ? role.getNome() : null;
  }

  public UsuarioResponseDTO() {
  }

  public UsuarioResponseDTO(Usuario usuario){
    this.id = usuario.getId();
    this.nome = usuario.getNome();
    this.email = usuario.getEmail();
    this.role = Optional.ofNullable(usuario.getRoleId()).map(Role::getNome).orElse("N/A");
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
