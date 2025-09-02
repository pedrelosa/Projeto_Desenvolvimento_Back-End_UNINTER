package br.com.SGHSS.VidaPlus.app.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TelemedicinaService {

    // Gera link de teleconsulta. Em produção: assinar URL, expirar token, etc.
    public String gerarLinkSeguroTeleconsulta(Long consultaId) {
        return "https://telemed.vidaplus/consulta/" + consultaId + "?tkn=" + UUID.randomUUID();
    }
}