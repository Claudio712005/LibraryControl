package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.model.Livro;

import java.util.logging.Logger;

public class LivroDAO extends GenericDAO<Livro> {

  private static final Logger logger = Logger.getLogger(LivroDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public LivroDAO(){
    super(Livro.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public boolean existsByTitulo(String titulo) {
    var resultSet = this.executeQuery("SELECT COUNT(*) FROM livros WHERE titulo = ?", titulo);

    try {
      if (resultSet.next()) {
        int count = resultSet.getInt(1);
        return count > 0;
      } else {
        logger.warning("Nenhum livro encontrado com o título: " + titulo);
        return false;
      }
    } catch (Exception e) {
      logger.severe("Erro ao verificar existência de livro por título: " + e.getMessage());
      return false;
    }
  }

  public Livro findByTitulo(String titulo) {
    var resultSet = this.executeQuery("SELECT * FROM livros WHERE titulo = ?", titulo);

    try {
      if (resultSet.next()) {
        Livro livro = new Livro();
        livro.setId(resultSet.getLong("id"));
        livro.setTitulo(resultSet.getString("titulo"));
        livro.setAnoPublicacao(resultSet.getInt("ano_publicacao"));
        livro.setDescricao(resultSet.getString("descricao"));
        return livro;
      } else {
        logger.warning("Nenhum livro encontrado com o título: " + titulo);
        return null;
      }
    } catch (Exception e) {
      logger.severe("Erro ao buscar livro por título: " + e.getMessage());
      return null;
    }
  }
}
