package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.usuario.Enfermeiro;
import br.com.SGHSS.VidaPlus.app.service.EnfermeiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enfermeiros")
public class EnfermeiroController {

    @Autowired private EnfermeiroService enfermeiroService;

    @PostMapping
    public ResponseEntity<Enfermeiro> salvar(@RequestBody Enfermeiro e) {
        return ResponseEntity.ok(enfermeiroService.salvar(e));
    }

    @PostMapping("/prontuarios/{prontuarioId}/sinais-vitais")
    public ResponseEntity<?> registrarSinaisVitais(
            @PathVariable Long prontuarioId,
            @RequestParam String anotacao) {
        return ResponseEntity.ok(enfermeiroService.registrarSinaisVitais(prontuarioId, anotacao));
    }
}