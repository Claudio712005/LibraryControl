package com.clau.service;

import com.clau.dao.AluguelLivroDAO;
import com.clau.dto.request.AluguelLivroRequestDTO;
import com.clau.dto.response.AluguelAtrasoResponseDTO;
import com.clau.dto.response.AluguelLivroResponseDTO;
import com.clau.enums.Role;
import com.clau.enums.Situacao;
import com.clau.exception.BadRequestException;
import com.clau.exception.NotFoundException;
import com.clau.model.AluguelLivro;
import com.clau.model.Livro;
import com.clau.model.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AluguelLivroService {

  private AluguelLivroDAO aluguelLivroDAO;

  private UsuarioService usuarioService;
  private LivroService livroService;

  public AluguelLivroService() {
    this.aluguelLivroDAO = new AluguelLivroDAO();
    this.usuarioService = new UsuarioService();
    this.livroService = new LivroService();
  }

  public List<AluguelLivro> buscarAlugueisPorUsuario(Long idUsuario) throws Exception {
    if (idUsuario == null) {
      throw new BadRequestException("O ID do usuário é obrigatório.");
    }

    List<AluguelLivro> alugueis = aluguelLivroDAO.buscarAlugueisPorUsuario(idUsuario);

    return alugueis;
  }

  public void efetuarAluguel(AluguelLivroRequestDTO requestDTO) throws Exception {
    if (requestDTO.getIdLivro() == null || requestDTO.getIdUsuario() == null || requestDTO.getDataDevolucao() == null) {
      throw new BadRequestException("Todos os campos são obrigatórios.");
    }

    if (requestDTO.getDataDevolucao().isBefore(LocalDateTime.now())) {
      throw new BadRequestException("A data de devolução não pode ser anterior à data atual.");
    }

    Livro livro = livroService.findById(requestDTO.getIdLivro());
    Usuario usuario = usuarioService.findById(requestDTO.getIdUsuario());

    if (Role.ADMIN.equals(usuario.getRoleId())) {
      throw new BadRequestException("Usuários com papel de ADMIN não podem alugar livros.");
    }

    if (!livro.getDisponivel()) {
      throw new BadRequestException("O livro selecionado não está disponível para aluguel.");
    }

    if (!aluguelLivroDAO.usuarioJaAlugouLivroNaoDevolvido(requestDTO.getIdUsuario(), requestDTO.getIdLivro()).isEmpty()) {
      throw new BadRequestException("O usuário já possui um aluguel ativo ou atrasado para este livro.");
    }

    AluguelLivro aluguelLivro = new AluguelLivro();
    aluguelLivro.setLivro(livro);
    aluguelLivro.setCliente(usuario);
    aluguelLivro.setDataHoraAluguel(LocalDateTime.now());
    aluguelLivro.setDataHoraDevolucao(requestDTO.getDataDevolucao());
    aluguelLivro.setRegistroObservacao(requestDTO.getRegistroObservacao());
    aluguelLivro.setSituacao(Situacao.ALUGADO);

    aluguelLivroDAO.save(aluguelLivro);
  }

  public void devolverLivro(Long idAluguel) {
    AluguelLivro aluguelLivro = findById(idAluguel);

    if (Situacao.DEVOLVIDO.equals(aluguelLivro.getSituacao())) {
      throw new BadRequestException("O livro já foi devolvido ou não está alugado.");
    }

    aluguelLivro.setSituacao(Situacao.DEVOLVIDO);

    aluguelLivroDAO.save(aluguelLivro);
  }

  public AluguelLivro findById(Long id) {
    return Optional.ofNullable(aluguelLivroDAO.findById(id)).orElseThrow(() ->
            new NotFoundException("Aluguel não encontrado com o ID: " + id));
  }

  public void removerAluguel(Long idAluguel) {
    AluguelLivro aluguelLivro = findById(idAluguel);

    if (aluguelLivro.getSituacao() != Situacao.ALUGADO) {
      throw new BadRequestException("O aluguel não pode ser cancelado, pois já foi devolvido ou não está ativo.");
    }

    aluguelLivroDAO.delete(aluguelLivro);
  }

  public void estenderAluguel(Long idAluguel, LocalDateTime novaDataDevolucao) {
    if (novaDataDevolucao.isBefore(LocalDateTime.now())) {
      throw new BadRequestException("A nova data de devolução não pode ser anterior à data atual.");
    }

    AluguelLivro aluguelLivro = findById(idAluguel);

    if (aluguelLivro.getDataHoraDevolucao() != null && novaDataDevolucao.isBefore(aluguelLivro.getDataHoraDevolucao())) {
      throw new BadRequestException("A nova data de devolução não pode ser anterior à data de devolução atual.");
    }

    if (aluguelLivro.getSituacao() != Situacao.ALUGADO) {
      throw new BadRequestException("O aluguel não está ativo e não pode ser estendido.");
    }

    aluguelLivro.setDataHoraDevolucao(novaDataDevolucao);
    aluguelLivroDAO.save(aluguelLivro);
  }

  public List<AluguelLivro> buscarAlugueisPorSituacao(Situacao situacao) throws Exception {
    if (situacao == null) {
      throw new BadRequestException("A situação não pode ser nula.");
    }

    return aluguelLivroDAO.buscarAlugueiPorSituacao(situacao);
  }

  public List<AluguelAtrasoResponseDTO> listarClientesComAtraso() throws Exception {
    List<AluguelLivro> alugueisAtrasados = buscarAlugueisPorSituacao(Situacao.ATRASADO);

    Map<Usuario, List<AluguelLivro>> usuariosComAlugueisAtrasados = alugueisAtrasados.stream()
            .collect(Collectors.groupingBy(AluguelLivro::getCliente));

    return usuariosComAlugueisAtrasados.entrySet().stream()
            .map(entry -> new AluguelAtrasoResponseDTO(entry.getKey(), entry.getValue()))
            .toList();
  }

  public void atrasarAluguel(Long idAluguel) {
    AluguelLivro aluguelLivro = findById(idAluguel);

    if (Situacao.DEVOLVIDO.equals(aluguelLivro.getSituacao())) {
      throw new BadRequestException("O livro já foi devolvido e não pode ser atrasado.");
    }

    aluguelLivro.setSituacao(Situacao.ATRASADO);
    aluguelLivroDAO.save(aluguelLivro);
  }

}
