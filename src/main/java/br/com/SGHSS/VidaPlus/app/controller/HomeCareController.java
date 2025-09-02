package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.homecare.HomeCare;
import br.com.SGHSS.VidaPlus.app.service.HomeCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/homecare")
public class HomeCareController {

    @Autowired private HomeCareService homeCareService;

    @PostMapping("/agendar")
    public ResponseEntity<HomeCare> agendar(
            @RequestParam Long pacienteId,
            @RequestParam Long enfermeiroId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            @RequestParam String procedimentos) {
        return ResponseEntity.ok(homeCareService.agendar(pacienteId, enfermeiroId, dataHora, procedimentos));
    }

    @PostMapping("/{id}/registrar-visita")
    public ResponseEntity<HomeCare> registrarVisita(@PathVariable Long id) {
        return ResponseEntity.ok(homeCareService.registrarVisita(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        homeCareService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}