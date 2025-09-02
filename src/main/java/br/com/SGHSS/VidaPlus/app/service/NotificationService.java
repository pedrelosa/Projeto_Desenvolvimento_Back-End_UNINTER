package br.com.SGHSS.VidaPlus.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void notificarPaciente(Long pacienteId, String mensagem) {
        // Stub: e-mail/SMS/Push. Aqui só loga.
        log.info("[NOTIFICAÇÃO] Paciente {} -> {}", pacienteId, mensagem);
    }

    public void notificarMedico(Long medicoId, String mensagem) {
        log.info("[NOTIFICAÇÃO] Medico {} -> {}", medicoId, mensagem);
    }
}