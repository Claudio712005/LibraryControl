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

}
