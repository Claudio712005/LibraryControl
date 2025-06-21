package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.model.Genero;

import java.util.logging.Logger;

public class GeneroDAO extends GenericDAO<Genero> {

  private static final Logger logger = Logger.getLogger(GenericDAO.class.getName());

  private DataBaseConfig dataBaseConfig;

  public GeneroDAO() {
    super(Genero.class);
    this.dataBaseConfig = new DataBaseConfig();
  }
}
