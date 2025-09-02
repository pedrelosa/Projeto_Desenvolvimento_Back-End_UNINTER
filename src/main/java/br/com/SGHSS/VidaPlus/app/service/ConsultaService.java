package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private AuditService auditService;

    public Consulta confirmar(Long consultaId) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada: " + consultaId));
        c.setStatus("Confirmada");
        Consulta salvo = consultaRepository.save(c);
        auditService.registrarAcao("CONFIRMAR_CONSULTA", "ConsultaId=" + salvo.getId());
        return salvo;
    }

    public Consulta cancelar(Long consultaId) {
        Consulta c = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada: " + consultaId));
        c.cancelarAgendamento();
        Consulta salvo = consultaRepository.save(c);
        auditService.registrarAcao("CANCELAR_CONSULTA", "ConsultaId=" + salvo.getId());
        return salvo;
    }

    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findAll().stream()
                .filter(c -> c.getPaciente() != null && c.getPaciente().getId().equals(pacienteId))
                .toList();
    }

    public List<Consulta> listarPorMedico(Long medicoId) {
        return consultaRepository.findAll().stream()
                .filter(c -> c.getMedico() != null && c.getMedico().getId().equals(medicoId))
                .toList();
    }

    public List<Consulta> listarPorData(LocalDate data) {
        return consultaRepository.findAll().stream()
                .filter(c -> c.getDataHora() != null && c.getDataHora().toLocalDate().equals(data))
                .toList();
    }
}