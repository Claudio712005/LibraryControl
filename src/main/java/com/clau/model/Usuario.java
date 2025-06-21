package com.clau.model;

import com.clau.enums.Role;
import com.clau.annotation.Column;
import com.clau.annotation.Id;
import com.clau.annotation.Table;

@Table(name = "usuarios")
public class Usuario {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "nome")
  private String nome;

  @Column(name = "email")
  private String email;

  @Column(name = "senha")
  private String senha;

  @Column(name = "role_id")
  private Role roleId;

  public Usuario() {
  }

  public Usuario(Long id, String nome, String email, String senha, Integer roleId) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.roleId = Role.fromId(roleId);
  }

  public Usuario(Long id, String nome, String email, String senha, Role role) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.roleId = role;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public String getSenha() {
    return senha;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Role getRoleId() {
    return roleId;
  }

  public void setRoleId(Role roleId) {
    this.roleId = roleId;
  }
}
