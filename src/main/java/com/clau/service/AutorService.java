package com.clau.service;

import com.clau.dao.AutorDAO;
import com.clau.exception.NotFoundException;
import com.clau.model.Autor;

import java.util.logging.Logger;

public class AutorService {

  private static Logger LOGGER = Logger.getLogger(AutorService.class.getName());

  private AutorDAO autorDAO;

  public AutorService() {
    this.autorDAO = new AutorDAO();
  }

  public Autor findById(Long id){
    Autor autor = autorDAO.findById(id);
    if (autor == null) {
      LOGGER.warning("Autor não encontrado com ID: " + id);
      throw new NotFoundException("Autor não encontrado com ID: " + id);
    }
    return autor;
  }
}
