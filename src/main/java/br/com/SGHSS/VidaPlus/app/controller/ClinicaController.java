package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.clinica.Clinica;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.service.ClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinicas")
public class ClinicaController {

    @Autowired private ClinicaService clinicaService;

    @PostMapping
    public ResponseEntity<Clinica> salvar(@RequestBody Clinica c) {
        return ResponseEntity.ok(clinicaService.salvar(c));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinica> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clinicaService.buscarPorId(id));
    }

    @PostMapping("/{id}/medicos")
    public ResponseEntity<Clinica> adicionarMedico(@PathVariable Long id, @RequestBody Medico medico) {
        return ResponseEntity.ok(clinicaService.adicionarMedico(id, medico));
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<ConsultaPresencial>> listarConsultas(@PathVariable Long id) {
        return ResponseEntity.ok(clinicaService.listarConsultas(id));
    }
}