package com.clau.service;

import com.clau.dao.UsuarioDAO;
import com.clau.dto.response.LoginResponseDTO;
import com.clau.exception.NotFoundException;
import com.clau.mapper.UsuarioMapper;
import com.clau.model.Usuario;
import com.clau.util.JwtUtil;

import java.util.List;
import java.util.logging.Logger;

public class UsuarioService {

  private static final Logger logger = Logger.getLogger(UsuarioService.class.getName());
  private UsuarioDAO usuarioDAO;
  private JwtUtil jwtUtil;

  public UsuarioService() {
    this.usuarioDAO = new UsuarioDAO();
    this.jwtUtil = new JwtUtil();
  }

  public List<Usuario> getUsuarios() {
    return usuarioDAO.findAll();
  }

  public LoginResponseDTO login(String email, String senha) {
    logger.info("Iniciando processo de login para o usuário: " + email);

    Usuario usuario = usuarioDAO.getUsuarioByEmailAndSenha(email, senha);

    if (usuario != null) {
      String token = jwtUtil.gerarToken(usuario.getEmail());
      logger.info("Usuário autenticado com sucesso: " + email);
      return new LoginResponseDTO(token, UsuarioMapper.toResponseDTO(usuario));
    } else {
      throw new NotFoundException("Usuário não encontrado com o email: " + email);
    }
  }
}
