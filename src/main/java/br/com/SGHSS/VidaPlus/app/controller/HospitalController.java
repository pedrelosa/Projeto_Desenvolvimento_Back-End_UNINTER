package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.hospital.Hospital;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitais")
public class HospitalController {

    @Autowired private HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<Hospital> salvar(@RequestBody Hospital h) {
        return ResponseEntity.ok(hospitalService.salvar(h));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hospital> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.buscarPorId(id));
    }

    @PostMapping("/{id}/medicos")
    public ResponseEntity<Hospital> adicionarMedico(@PathVariable Long id, @RequestBody Medico medico) {
        return ResponseEntity.ok(hospitalService.adicionarMedico(id, medico));
    }

    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<ConsultaPresencial>> listarConsultas(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.listarConsultas(id));
    }
}