package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired private ConsultaService consultaService;

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Consulta> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.confirmar(id));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Consulta> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.cancelar(id));
    }

    @GetMapping
    public ResponseEntity<List<Consulta>> listar(
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        if (pacienteId != null) return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId));
        if (medicoId != null) return ResponseEntity.ok(consultaService.listarPorMedico(medicoId));
        if (data != null) return ResponseEntity.ok(consultaService.listarPorData(data));
        return ResponseEntity.ok(consultaService.listarPorData(LocalDate.now()));
    }
}