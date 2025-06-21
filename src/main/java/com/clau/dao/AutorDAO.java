package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.model.Autor;

import java.util.logging.Logger;

public class AutorDAO extends GenericDAO<Autor> {

  private static final Logger logger = Logger.getLogger(AutorDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public AutorDAO() {
    super(Autor.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

}
