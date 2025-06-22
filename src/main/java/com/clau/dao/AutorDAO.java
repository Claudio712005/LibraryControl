package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.exception.AppDataException;
import com.clau.model.Autor;

import java.sql.SQLException;
import java.util.logging.Logger;

public class AutorDAO extends GenericDAO<Autor> {

  private static final Logger logger = Logger.getLogger(AutorDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public AutorDAO() {
    super(Autor.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public boolean existsByNome(String nome) {
    var resultSet = this.executeQuery("SELECT COUNT(*) FROM autores WHERE nome = ?", nome);
    try {
      if (resultSet.next()) {
        int count = resultSet.getInt(1);
        return count > 0;
      } else {
        logger.warning("Nenhum autor encontrado com o nome: " + nome);
        return false;
      }
    } catch (SQLException e) {
      logger.severe("Erro ao verificar existência de autor: " + e.getMessage());
      throw new AppDataException("Erro ao verificar existência de autor: " + e.getMessage(), e);
    }
  }

  public Autor findByNome(String nome) {
    var resultSet = this.executeQuery("SELECT * FROM autores WHERE nome = ?", nome);
    try {
      if (resultSet.next()) {
        return this.mapResultSet(resultSet);
      } else {
        logger.warning("Nenhum autor encontrado com o nome: " + nome);
        return null;
      }
    } catch (Exception e) {
      logger.severe("Erro ao buscar autor por nome: " + e.getMessage());
      throw new AppDataException("Erro ao buscar autor por nome: " + e.getMessage(), e);
    }
  }
}
