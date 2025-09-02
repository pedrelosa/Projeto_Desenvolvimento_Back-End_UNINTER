package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import br.com.SGHSS.VidaPlus.app.repository.prontuario.ProntuarioRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProntuarioService {

    @Autowired private ProntuarioRepository prontuarioRepository;
    @Autowired private AuditService auditService;

    public Prontuario criar(Prontuario p) {
        p.setDataCriacao(LocalDateTime.now());
        Prontuario salvo = prontuarioRepository.save(p);
        auditService.registrarAcao("CRIAR_PRONTUARIO", "ProntuarioId=" + salvo.getId());
        return salvo;
    }

    public Prontuario adicionarAnotacao(Long prontuarioId, String anotacao) {
        Prontuario p = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado: " + prontuarioId));

        String prev = p.getDiagnostico() == null ? "" : p.getDiagnostico() + "\n";
        p.setDiagnostico(prev + "[Anotação " + LocalDateTime.now() + "] " + anotacao);

        Prontuario salvo = prontuarioRepository.save(p);
        auditService.registrarAcao("ADICIONAR_ANOTACAO", "ProntuarioId=" + prontuarioId);
        return salvo;
    }

    public String visualizar(Long prontuarioId) {
        Prontuario p = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado: " + prontuarioId));

        return """
               Diagnóstico: %s
               Prescrições: %s
               Criado em: %s
               """.formatted(p.getDiagnostico(), p.getPrescricoes(), p.getDataCriacao());
    }
}