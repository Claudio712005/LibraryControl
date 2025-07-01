package com.clau.dao;

import com.clau.config.dao.GenericDAO;
import com.clau.config.database.DataBaseConfig;
import com.clau.enums.Situacao;
import com.clau.model.AluguelLivro;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class AluguelLivroDAO extends GenericDAO<AluguelLivro> {

  private DataBaseConfig dataBaseConfig;

  public AluguelLivroDAO() {
    super(AluguelLivro.class);
    this.dataBaseConfig = new DataBaseConfig();
  }

  public List<AluguelLivro> buscarAlugueisPorUsuario(Long idUsuario) throws Exception {
    String query = "SELECT * FROM aluguel_livro WHERE cliente_id = ?";

    ResultSet resultSet = this.executeQuery(query, idUsuario);
    return this.mapResultSetList(resultSet);
  }

  public List<AluguelLivro> usuarioJaAlugouLivroNaoDevolvido(Long idUsuario, Long idLivro) throws Exception {
    String query = "SELECT * FROM aluguel_livro WHERE cliente_id = ? AND livro_id = ? AND situacao IN ('ALUGADO', 'ATRASADO')";

    ResultSet resultSet = this.executeQuery(query, idUsuario, idLivro);
    return this.mapResultSetList(resultSet);
  }

  public List<AluguelLivro> buscarAlugueiPorSituacao(Situacao situacao) throws Exception {

    if (situacao == null) {
      throw new IllegalArgumentException("A situação não pode ser nula.");
    }

    String query = "SELECT * FROM aluguel_livro WHERE situacao = ?";

    ResultSet resultSet = this.executeQuery(query, situacao.getNome());
    return this.mapResultSetList(resultSet);
  }
}
