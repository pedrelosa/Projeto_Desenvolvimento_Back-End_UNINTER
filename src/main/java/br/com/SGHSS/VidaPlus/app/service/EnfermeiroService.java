package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import br.com.SGHSS.VidaPlus.app.model.usuario.Enfermeiro;
import br.com.SGHSS.VidaPlus.app.repository.prontuario.ProntuarioRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.EnfermeiroRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnfermeiroService {

    @Autowired private EnfermeiroRepository enfermeiroRepository;
    @Autowired private ProntuarioRepository prontuarioRepository;
    @Autowired private AuditService auditService;

    public Enfermeiro salvar(Enfermeiro e) {
        Enfermeiro salvo = enfermeiroRepository.save(e);
        auditService.registrarAcao("CADASTRO_ENFERMEIRO", "EnfermeiroId=" + salvo.getId());
        return salvo;
    }

    @Transactional
    public Prontuario registrarSinaisVitais(Long prontuarioId, String anotacao) {
        // Dado que a entidade não possui campo específico de sinais vitais,
        // vamos anexar a anotação no diagnóstico.
        Prontuario p = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Prontuário não encontrado: " + prontuarioId));

        String prev = p.getDiagnostico() == null ? "" : p.getDiagnostico() + "\n";
        p.setDiagnostico(prev + "[Sinais Vitais] " + anotacao);

        Prontuario salvo = prontuarioRepository.save(p);
        auditService.registrarAcao("REGISTRAR_SINAIS_VITAIS", "ProntuarioId=" + prontuarioId);
        return salvo;
    }
}
