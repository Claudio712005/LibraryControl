package com.clau.service;

import com.clau.dao.LivroDAO;
import com.clau.dto.request.LivroRequestDTO;
import com.clau.exception.ConflictException;
import com.clau.exception.NotFoundException;
import com.clau.model.Autor;
import com.clau.model.Genero;
import com.clau.model.Livro;

import java.util.List;
import java.util.logging.Logger;

public class LivroService {

  private static final Logger logger = Logger.getLogger(LivroService.class.getName());

  private LivroDAO livroDAO;
  private AutorService autorService;
  private GeneroService generoService;

  public LivroService() {
    this.livroDAO = new LivroDAO();
    this.autorService = new AutorService();
    this.generoService = new GeneroService();
  }

  public List<Livro> findAll(){
    return livroDAO.findAll();
  }

  public Livro findById(Long id) {
    Livro livro = livroDAO.findById(id);
    if (livro == null) {
      logger.warning("Livro com ID " + id + " não encontrado.");
      throw new NotFoundException("Livro não encontrado");
    }
    return livro;
  }

  public void cadastrarLisvro(LivroRequestDTO requestDTO){

    if (livroDAO.existsByTitulo(requestDTO.getTitulo())) {
      logger.warning("Livro com título '" + requestDTO.getTitulo() + "' já existe.");
      throw new ConflictException("Livro já cadastrado com este título.");
    }

    Autor autor = autorService.findById(requestDTO.getIdAutor());

    Genero genero = generoService.findById(requestDTO.getIdGenero());

    Livro livro = new Livro();
    livro.setTitulo(requestDTO.getTitulo());
    livro.setAutor(autor);
    livro.setAnoPublicacao(requestDTO.getAnoPublicacao());
    livro.setGenero(genero);
    livro.setDescricao(requestDTO.getDescricao());

    livroDAO.save(livro);
  }

  public void atualizarLivro(Long id, LivroRequestDTO requestDTO) {
    Livro livroExistente = findById(id);

    Autor autor = autorService.findById(requestDTO.getIdAutor());
    Genero genero = generoService.findById(requestDTO.getIdGenero());

    Livro livroDuplicado = livroDAO.findByTitulo(requestDTO.getTitulo());
    if (livroDuplicado != null && !livroDuplicado.getId().equals(id)) {
      logger.warning("Livro com título '" + requestDTO.getTitulo() + "' já existe.");
      throw new ConflictException("Já existe um livro cadastrado com este título.");
    }

    livroExistente.setTitulo(requestDTO.getTitulo());
    livroExistente.setAutor(autor);
    livroExistente.setAnoPublicacao(requestDTO.getAnoPublicacao());
    livroExistente.setGenero(genero);
    livroExistente.setDescricao(requestDTO.getDescricao());

    livroDAO.save(livroExistente);
  }

  public void excluirLivro(Long id) {
    Livro livro = findById(id);
    livroDAO.delete(livro);
    logger.info("Livro com ID " + id + " excluído com sucesso.");
  }
}
