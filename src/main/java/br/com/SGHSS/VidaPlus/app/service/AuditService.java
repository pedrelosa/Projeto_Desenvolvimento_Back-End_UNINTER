package br.com.SGHSS.VidaPlus.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    public void registrarAcao(String acao, String detalhe) {
        // Em produção: persistir em tabela de auditoria
        log.info("[AUDIT] {} -> {}", acao, detalhe);
    }
}
