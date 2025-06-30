package com.clau.scheduler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AtrasoScheduler {

  private static final Logger LOGGER = Logger.getLogger(AtrasoScheduler.class.getName());

  public void scheduleAtrasos() {
    try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()) {
      Runnable tarefa = () -> LOGGER.info("Iniciando verificação de atrasos nos aluguéis dos livros...");

      long initDelay = Duration.between(
              LocalDateTime.now(), LocalDateTime.now().toLocalDate()
                      .plusDays(1).atStartOfDay()).getSeconds();

      long intervalo = TimeUnit.DAYS.toSeconds(1);

      scheduler.scheduleAtFixedRate(tarefa, initDelay, intervalo, TimeUnit.SECONDS);

    } catch (Exception e) {
      LOGGER.severe("Erro ao agendar verificação de atrasos: " + e.getMessage());
    } finally {
      LOGGER.info("Agendamento de verificação de atrasos finalizado as " + LocalDateTime.now());
    }

  }
}
