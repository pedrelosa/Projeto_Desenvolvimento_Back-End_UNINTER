package br.com.SGHSS.VidaPlus.app.model;

import java.time.LocalDateTime;

public interface IAgendavel {
    void agendar(LocalDateTime dataHora);
    void cancelarAgendamento();
}
