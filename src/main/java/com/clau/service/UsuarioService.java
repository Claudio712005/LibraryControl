package com.clau.service;

import com.clau.dao.UsuarioDAO;
import com.clau.model.Usuario;

import java.util.List;
import java.util.logging.Logger;

public class UsuarioService {

  private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());

  private UsuarioDAO usuarioDAO;

  public UsuarioService () {
    this.usuarioDAO = new UsuarioDAO();
  }

  public List<Usuario> getUsuarios(){
    return usuarioDAO.findAll();
  }
}
