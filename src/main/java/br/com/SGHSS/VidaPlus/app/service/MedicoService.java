package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaRepository;
import br.com.SGHSS.VidaPlus.app.repository.prontuario.ProntuarioRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.MedicoRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import br.com.SGHSS.VidaPlus.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicoService {

    @Autowired private MedicoRepository medicoRepository;
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private ProntuarioRepository prontuarioRepository;

    @Autowired private NotificationService notificationService;
    @Autowired private AuditService auditService;

    public Medico salvar(Medico medico) {
        Medico salvo = medicoRepository.save(medico);
        auditService.registrarAcao("CADASTRO_MEDICO", "MedicoId=" + salvo.getId());
        return salvo;
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado: " + id));
    }

    public List<Consulta> listarConsultasPorData(Long medicoId, LocalDate data) {
        Medico m = buscarPorId(medicoId);
        // Filtro em memória (simples). Em produção: query no repository por intervalo.
        return m.getConsultas().stream()
                .filter(c -> c.getDataHora().toLocalDate().equals(data))
                .toList();
    }

    @Transactional
    public Prontuario atualizarProntuario(Long prontuarioId, String novoDiagnostico, String novasPrescricoes) {
        Prontuario p = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado: " + prontuarioId));

        if (novoDiagnostico != null && !novoDiagnostico.isBlank()) p.setDiagnostico(novoDiagnostico);
        if (novasPrescricoes != null && !novasPrescricoes.isBlank()) p.setPrescricoes(novasPrescricoes);

        Prontuario salvo = prontuarioRepository.save(p);
        auditService.registrarAcao("ATUALIZAR_PRONTUARIO", "ProntuarioId=" + prontuarioId);
        return salvo;
    }

    @Transactional
    public Prontuario emitirReceita(Long prontuarioId, String receitaDigital) {
        Prontuario p = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado: " + prontuarioId));

        String prev = p.getPrescricoes() == null ? "" : p.getPrescricoes() + "\n";
        p.setPrescricoes(prev + "[Receita " + LocalDateTime.now() + "] " + receitaDigital);

        Prontuario salvo = prontuarioRepository.save(p);
        notificationService.notificarPaciente(p.getPaciente().getId(), "Sua receita digital foi emitida.");
        auditService.registrarAcao("EMITIR_RECEITA", "ProntuarioId=" + prontuarioId);
        return salvo;
    }
}
