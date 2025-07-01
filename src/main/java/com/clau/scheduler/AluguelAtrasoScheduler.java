package com.clau.scheduler;

import com.clau.enums.Situacao;
import com.clau.model.AluguelLivro;
import com.clau.service.AluguelLivroService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AluguelAtrasoScheduler {

  private static final Logger LOGGER = Logger.getLogger(AluguelAtrasoScheduler.class.getName());

  private final AluguelLivroService aluguelLivroService = new AluguelLivroService();
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

  public void iniciar() {
    Runnable tarefa = () -> {
      try {
        System.out.println("Verificando alugueis atrasados: " + LocalDateTime.now());

        List<AluguelLivro> alugueis = aluguelLivroService.buscarAlugueisPorSituacao(Situacao.ALUGADO);

        for (AluguelLivro aluguel : alugueis) {
          if (aluguel.getDataHoraDevolucao() != null && aluguel.getDataHoraDevolucao().isBefore(LocalDateTime.now())) {
            aluguelLivroService.atrasarAluguel(aluguel.getId());
            LOGGER.warning("Aluguel atrasado: " + aluguel.getId() + " - Livro: " + aluguel.getLivro().getTitulo() + " - Cliente: " + aluguel.getCliente().getNome());
          }
        }

        System.out.println("Verificação de alugueis atrasados concluída: " + LocalDateTime.now());

      } catch (Exception e) {
        e.printStackTrace();
      }
    };

    scheduler.scheduleAtFixedRate(tarefa, 0, 20, TimeUnit.SECONDS);
  }

  public void parar() {
    scheduler.shutdown();
  }
}
