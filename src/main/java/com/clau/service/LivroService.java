package com.clau.service;

import com.clau.dao.LivroDAO;
import com.clau.model.Livro;

import java.util.List;
import java.util.logging.Logger;

public class LivroService {

  private static final Logger logger = Logger.getLogger(LivroService.class.getName());

  private LivroDAO livroDAO;

  public LivroService() {
    this.livroDAO = new LivroDAO();
  }

  public List<Livro> findAll(){
    return livroDAO.findAll();
  }
}
