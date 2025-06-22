package com.clau.service;

import com.clau.dao.UsuarioDAO;
import com.clau.dto.request.UsuarioRequestDTO;
import com.clau.dto.response.LoginResponseDTO;
import com.clau.exception.AppDataException;
import com.clau.exception.BadRequestException;
import com.clau.exception.ConflictException;
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

    Usuario usuario = null;

    try{
      usuario = usuarioDAO.getUsuarioByEmailAndSenha(email, senha);
    } catch (Exception e) {
      throw new AppDataException("Erro ao buscar usuário: " + e.getMessage());
    }

    if (usuario != null) {
      String token = jwtUtil.gerarToken(usuario.getEmail(), usuario.getRoleId());
      logger.info("Usuário autenticado com sucesso: " + email);
      return new LoginResponseDTO(token, UsuarioMapper.toResponseDTO(usuario));
    } else {
      throw new NotFoundException("Usuário não encontrado com o email: " + email);
    }
  }

  public void cadastrar(UsuarioRequestDTO requestDTO){
    if(requestDTO == null || requestDTO.getNome() == null || requestDTO.getEmail() == null || requestDTO.getSenha() == null) {
      throw new BadRequestException("Dados do usuário inválidos para cadastro.");
    }

    if(!requestDTO.getSenha().equals(requestDTO.getConfirmacaoSenha())){
      throw new BadRequestException("As senhas não coincidem.");
    }

    Usuario existente = usuarioDAO.findByEmail(requestDTO.getEmail());

    if (existente != null) {
      throw new ConflictException("Já existe um usuário cadastrado com o email: " + requestDTO.getEmail());
    }

    usuarioDAO.save(UsuarioMapper.toEntity(requestDTO));
  }
}
