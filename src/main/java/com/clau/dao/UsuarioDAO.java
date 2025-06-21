package com.clau.dao;

import com.clau.enums.Role;
import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.exception.AppDataException;
import com.clau.model.Usuario;

import java.sql.SQLException;
import java.util.logging.Logger;

public class UsuarioDAO extends GenericDAO<Usuario> {

  private final static Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public UsuarioDAO() {
    super(Usuario.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public Usuario getUsuarioByEmailAndSenha(String email, String senha) {
    Usuario usuario = null;

    var resultSet = this.executeQuery("SELECT * FROM usuarios WHERE email = ? AND senha = ?", email, senha);

    try {
      if (resultSet.next()) {
        usuario = new Usuario();
        usuario.setId(resultSet.getLong("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
        usuario.setRoleId(Role.fromNome(resultSet.getString("role_id")));
      } else {
        logger.warning("Nenhum usuário encontrado com o email e senha informados: " + email);
      }
    } catch (SQLException e) {
      throw new AppDataException("Erro ao buscar usuário: " + e.getMessage(), e);
    }

    return usuario;
  }

  public Usuario findByEmail(String email) {
    Usuario usuario = null;

    var resultSet = this.executeQuery("SELECT * FROM usuarios WHERE email = ?", email);

    try {
      if (resultSet.next()) {
        usuario = new Usuario();
        usuario.setId(resultSet.getLong("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
      }
    } catch (SQLException e) {
      throw new AppDataException("Erro ao buscar usuário por email: " + e.getMessage(), e);
    }

    return usuario;
  }
}
