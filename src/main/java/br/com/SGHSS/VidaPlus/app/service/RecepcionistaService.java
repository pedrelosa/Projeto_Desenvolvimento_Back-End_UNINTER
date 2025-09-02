package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import br.com.SGHSS.VidaPlus.app.model.usuario.Recepcionista;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaPresencialRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.PacienteRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.RecepcionistaRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import br.com.SGHSS.VidaPlus.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecepcionistaService {

    @Autowired private RecepcionistaRepository recepcionistaRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private ConsultaPresencialRepository consultaPresencialRepository;
    @Autowired private NotificationService notificationService;
    @Autowired private AuditService auditService;

    public Recepcionista salvar(Recepcionista r) {
        Recepcionista salvo = recepcionistaRepository.save(r);
        auditService.registrarAcao("CADASTRO_RECEPCIONISTA", "RecepcionistaId=" + salvo.getId());
        return salvo;
    }

    public Paciente cadastrarPaciente(Paciente p) {
        Paciente salvo = pacienteRepository.save(p);
        auditService.registrarAcao("CADASTRO_PACIENTE_BY_RECEP", "PacienteId=" + salvo.getId());
        return salvo;
    }

    public ConsultaPresencial agendarConsulta(Long pacienteId, Long medicoId, LocalDateTime dataHora, String sala) {
        // Encaminha para regra do Paciente para manter consistência
        // (poderia chamar PacienteService, mas para evitar dependência circular, replica-se o essencial)
        ConsultaPresencial c = new ConsultaPresencial();
        c.setDataHora(dataHora);
        c.setStatus("Agendada");
        c.setSala(sala);
        // Paciente e Médico devem ser setados pelo Controller usando services adequados ou via repositórios
        ConsultaPresencial salva = consultaPresencialRepository.save(c);
        notificationService.notificarPaciente(pacienteId, "Consulta presencial agendada para " + dataHora);
        auditService.registrarAcao("AGENDAR_CONSULTA_RECEP", "ConsultaId=" + salva.getId());
        return salva;
    }
}