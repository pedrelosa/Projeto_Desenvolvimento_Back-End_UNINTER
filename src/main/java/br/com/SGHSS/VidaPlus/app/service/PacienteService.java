package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaOnline;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaOnlineRepository;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaPresencialRepository;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaRepository;
import br.com.SGHSS.VidaPlus.app.repository.prontuario.ProntuarioRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.MedicoRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PacienteService {

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private MedicoRepository medicoRepository;
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private ConsultaPresencialRepository consultaPresencialRepository;
    @Autowired private ConsultaOnlineRepository consultaOnlineRepository;
    @Autowired private ProntuarioRepository prontuarioRepository;
    @Autowired private NotificationService notificationService;
    @Autowired private AuditService auditService;
    @Autowired private TelemedicinaService telemedicinaService;

    public Paciente cadastrar(Paciente p) {
        Paciente salvo = pacienteRepository.save(p);
        auditService.registrarAcao("CADASTRAR_PACIENTE", "PacienteId=" + salvo.getId());
        return salvo;
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado: " + id));
    }

    public List<Prontuario> visualizarHistoricoClinico(Long pacienteId) {
        return prontuarioRepository.findAll().stream()
                .filter(p -> p.getPaciente() != null && p.getPaciente().getId().equals(pacienteId))
                .toList();
    }

    @Transactional
    public ConsultaPresencial agendarConsultaPresencial(Long pacienteId, Long medicoId, LocalDateTime dataHora, String sala) {
        Paciente paciente = buscarPorId(pacienteId);
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado: " + medicoId));

        ConsultaPresencial c = new ConsultaPresencial();
        c.setPaciente(paciente);
        c.setMedico(medico);
        c.agendar(dataHora);
        c.setStatus("Agendada");
        c.setSala(sala);

        ConsultaPresencial salva = consultaPresencialRepository.save(c);
        notificationService.notificarPaciente(paciente.getId(), "Consulta presencial agendada para " + dataHora);
        notificationService.notificarMedico(medico.getId(), "Nova consulta presencial agendada para " + dataHora);
        auditService.registrarAcao("AGENDAR_CONSULTA_PRESENCIAL", "ConsultaId=" + salva.getId());
        return salva;
    }

    @Transactional
    public ConsultaOnline agendarConsultaOnline(Long pacienteId, Long medicoId, LocalDateTime dataHora) {
        Paciente paciente = buscarPorId(pacienteId);
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado: " + medicoId));

        ConsultaOnline c = new ConsultaOnline();
        c.setPaciente(paciente);
        c.setMedico(medico);
        c.agendar(dataHora);
        c.setStatus("Agendada");
        // Link é gerado no acesso (método acessarTeleconsulta)

        ConsultaOnline salva = consultaOnlineRepository.save(c);
        notificationService.notificarPaciente(paciente.getId(), "Consulta online agendada para " + dataHora);
        notificationService.notificarMedico(medico.getId(), "Nova consulta online agendada para " + dataHora);
        auditService.registrarAcao("AGENDAR_CONSULTA_ONLINE", "ConsultaId=" + salva.getId());
        return salva;
    }

    @Transactional
    public void cancelarConsulta(Long consultaId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada: " + consultaId));
        consulta.cancelarAgendamento();
        consultaRepository.save(consulta);
        notificationService.notificarPaciente(consulta.getPaciente().getId(), "Sua consulta foi cancelada.");
        notificationService.notificarMedico(consulta.getMedico().getId(), "Uma consulta foi cancelada.");
        auditService.registrarAcao("CANCELAR_CONSULTA", "ConsultaId=" + consultaId);
    }

    public String acessarTeleconsulta(Long consultaOnlineId) {
        String link = telemedicinaService.gerarLinkSeguroTeleconsulta(consultaOnlineId);
        auditService.registrarAcao("ACESSAR_TELECONSULTA", "ConsultaId=" + consultaOnlineId);
        return link;
    }
}