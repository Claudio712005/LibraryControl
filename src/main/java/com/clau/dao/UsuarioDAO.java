package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.exception.AppDataException;
import com.clau.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class UsuarioDAO extends GenericDAO<Usuario> {

  private final static Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public UsuarioDAO() {
    super(Usuario.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public Usuario getUsuarioByEmailAndSenha(String email, String senha){
    Usuario usuario = null;

    try(Connection con = DataBaseConfig.getConnection()){
      String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

      PreparedStatement statement = con.prepareStatement(sql);
      statement.setString(1, email);
      statement.setString(2, senha);

      var resultSet = statement.executeQuery();

      if(resultSet.next()){
        usuario = new Usuario();
        usuario.setId(resultSet.getLong("id"));
        usuario.setNome(resultSet.getString("nome"));
        usuario.setEmail(resultSet.getString("email"));
        usuario.setSenha(resultSet.getString("senha"));
      } else {
        logger.warning("Nenhum usuário encontrado com o email e senha informados: " + email);
      }

    } catch (AppDataException e){
      logger.severe("Erro ao obter conexão com o banco de dados: " + e.getMessage());
      throw new RuntimeException("Erro ao obter conexão com o banco de dados", e);
    } catch (Exception e) {
      logger.severe("Erro ao buscar usuário por email e senha: " + e.getMessage());
      throw new RuntimeException("Erro ao buscar usuário por email e senha", e);
    }

    return usuario;
  }
}
