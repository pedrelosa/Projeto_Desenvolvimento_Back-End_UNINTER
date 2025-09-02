package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired private MedicoService medicoService;

    @PostMapping
    public ResponseEntity<Medico> salvar(@RequestBody Medico medico) {
        return ResponseEntity.ok(medicoService.salvar(medico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<Consulta>> listarConsultasPorData(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(medicoService.listarConsultasPorData(id, data));
    }

    @PutMapping("/prontuarios/{prontuarioId}")
    public ResponseEntity<?> atualizarProntuario(
            @PathVariable Long prontuarioId,
            @RequestParam(required = false) String diagnostico,
            @RequestParam(required = false) String prescricoes) {
        return ResponseEntity.ok(medicoService.atualizarProntuario(prontuarioId, diagnostico, prescricoes));
    }

    @PostMapping("/prontuarios/{prontuarioId}/receitas")
    public ResponseEntity<?> emitirReceita(
            @PathVariable Long prontuarioId,
            @RequestParam String receitaDigital) {
        return ResponseEntity.ok(medicoService.emitirReceita(prontuarioId, receitaDigital));
    }
}