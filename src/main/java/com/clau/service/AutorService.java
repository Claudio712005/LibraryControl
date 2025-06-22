package com.clau.service;

import com.clau.dao.AutorDAO;
import com.clau.dto.request.AutorRequestDTO;
import com.clau.exception.ConflictException;
import com.clau.exception.NotFoundException;
import com.clau.model.Autor;

import java.util.List;
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

  public List<Autor> findAll() {
    List<Autor> autores = autorDAO.findAll();
    return autores;
  }

  public void cadastrarAutor(AutorRequestDTO requestDTO){
    if (requestDTO == null || requestDTO.getNome() == null || requestDTO.getNome().isEmpty()) {
      throw new IllegalArgumentException("O nome do autor não pode ser nulo ou vazio.");
    }

    if (autorDAO.existsByNome(requestDTO.getNome())) {
      LOGGER.warning("Autor já cadastrado com o nome: " + requestDTO.getNome());
      throw new ConflictException("Autor já cadastrado com o nome: " + requestDTO.getNome());
    }

    Autor novoAutor = new Autor();
    novoAutor.setNome(requestDTO.getNome());

    autorDAO.save(novoAutor);
    LOGGER.info("Autor cadastrado com sucesso: " + novoAutor.getNome());
  }

  public void atualizarAutor(AutorRequestDTO requestDTO, Long id){
    if (requestDTO == null || id == null) {
      throw new IllegalArgumentException("O ID do autor não pode ser nulo.");
    }

    Autor autorExistente = findById(id);
    if (autorExistente == null) {
      LOGGER.warning("Autor não encontrado com ID: " + id);
      throw new NotFoundException("Autor não encontrado com ID: " + id);
    }

    autorExistente.setNome(requestDTO.getNome());

    Autor autorDuplicado = autorDAO.findByNome(requestDTO.getNome());

    if(autorDuplicado != null && !autorDuplicado.getId().equals(id)) {
      LOGGER.warning("Já existe um autor cadastrado com o nome: " + requestDTO.getNome());
      throw new ConflictException("Já existe um autor cadastrado com o nome: " + requestDTO.getNome());
    }

    autorDAO.save(autorExistente);
  }

  public void delete(Long id) {
    Autor autor = findById(id);
    if (autor == null) {
      LOGGER.warning("Autor não encontrado com ID: " + id);
      throw new NotFoundException("Autor não encontrado com ID: " + id);
    }
    autorDAO.delete(autor);
    LOGGER.info("Autor deletado com sucesso: " + autor.getNome());
  }

}
