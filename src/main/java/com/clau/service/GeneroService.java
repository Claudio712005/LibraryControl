package com.clau.service;

import com.clau.dao.GeneroDAO;
import com.clau.dto.request.GeneroRequestDTO;
import com.clau.exception.ConflictException;
import com.clau.exception.NotFoundException;
import com.clau.model.Genero;

import java.util.List;
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

  public List<Genero> findAll(){
    return generoDAO.findAll();
  }

  public void cadastrarGenero(GeneroRequestDTO requestDTO){
    Genero genero = new Genero(requestDTO);

    if(generoDAO.existsByNome(genero.getNome())) {
      logger.warning("Gênero já cadastrado: " + genero.getNome());
      throw new ConflictException("Gênero já cadastrado com o nome: " + genero.getNome());
    }

    generoDAO.save(genero);
  }

  public void atualizarGenero(GeneroRequestDTO requestDTO, Long id) {
    Genero genero = findById(id);

    genero.setNome(requestDTO.getNome());
    genero.setDescricao(requestDTO.getDescricao());

    Genero generoDuplicado = generoDAO.findByNome(genero.getNome());

    if(generoDuplicado != null && !generoDuplicado.getId().equals(genero.getId())) {
      logger.warning("Gênero já cadastrado com o nome: " + genero.getNome());
      throw new ConflictException("Gênero já cadastrado com o nome: " + genero.getNome());
    }

    generoDAO.save(genero);
  }

  public void excluirGenero(Long id) {
    Genero genero = findById(id);
    generoDAO.delete(genero);
    logger.info("Gênero excluído com sucesso: " + genero.getNome());
  }

}
