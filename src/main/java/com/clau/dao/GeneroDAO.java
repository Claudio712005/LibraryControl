package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.exception.AppDataException;
import com.clau.model.Genero;

import java.sql.SQLException;
import java.util.logging.Logger;

public class GeneroDAO extends GenericDAO<Genero> {

  private static final Logger logger = Logger.getLogger(GenericDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public GeneroDAO() {
    super(Genero.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public boolean existsByNome(String nome){
    var resultSet = this.executeQuery("SELECT COUNT(*) FROM generos WHERE nome = ?", nome);

    try{
      if(resultSet.next()){
        int count = resultSet.getInt(1);
        return count > 0;
      } else {
        logger.warning("Nenhum gênero encontrado com o nome: " + nome);
        return false;
      }
    }catch (SQLException e) {
      throw new AppDataException("Erro ao contar gêneros por nome: " + e.getMessage(), e);
    }
  }

  public Genero findByNome(String nome){
    var resultSet = this.executeQuery("SELECT * FROM generos WHERE nome = ?", nome);

    try {
      if (resultSet.next()) {
        Genero genero = new Genero();
        genero.setId(resultSet.getLong("id"));
        genero.setNome(resultSet.getString("nome"));
        genero.setDescricao(resultSet.getString("descricao"));
        return genero;
      } else {
        logger.warning("Nenhum gênero encontrado com o nome: " + nome);
        return null;
      }
    } catch (SQLException e) {
      throw new AppDataException("Erro ao buscar gênero por nome: " + e.getMessage(), e);
    }
  }
}
