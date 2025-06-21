package com.clau.mapper;

import com.clau.dto.request.UsuarioRequestDTO;
import com.clau.dto.response.UsuarioResponseDTO;
import com.clau.model.Usuario;

public class UsuarioMapper {

  public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
    return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getRoleId()
    );
  }

  public static Usuario toEntity(UsuarioRequestDTO requestDTO){
    return new Usuario(
            null,
            requestDTO.getNome(),
            requestDTO.getEmail(),
            requestDTO.getSenha(),
            requestDTO.getRole()
    );
  }
}
