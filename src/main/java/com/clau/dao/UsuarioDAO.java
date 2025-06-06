package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.model.Usuario;

import java.sql.Connection;
import java.util.logging.Logger;

public class UsuarioDAO extends GenericDAO<Usuario> {

  private final static Logger logger = Logger.getLogger(UsuarioDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public UsuarioDAO() {
    super(Usuario.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public String getUsuarios() {
    logger.info("Buscando usuários no banco de dados...");

    Connection connection = DataBaseConfig.getConnection();

    return "Lista de usuários";
  }
}
