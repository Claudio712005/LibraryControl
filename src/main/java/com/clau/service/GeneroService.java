package com.clau.service;

import com.clau.dao.GeneroDAO;
import com.clau.exception.NotFoundException;
import com.clau.model.Genero;

import java.util.logging.Logger;

public class GeneroService {

  private static Logger logger = Logger.getLogger(GeneroService.class.getName());

  private GeneroDAO generoDAO;

  public GeneroService() {
    this.generoDAO = new GeneroDAO();
  }

  public Genero findById(Long id) {
    Genero genero = generoDAO.findById(id);
    if (genero == null) {
      logger.warning("Gênero não encontrado com ID: " + id);
      throw new NotFoundException("Gênero não encontrado com ID: " + id);
    }
    return genero;
  }
}
