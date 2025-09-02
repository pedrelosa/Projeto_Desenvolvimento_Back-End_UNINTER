package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import br.com.SGHSS.VidaPlus.app.model.usuario.Recepcionista;
import br.com.SGHSS.VidaPlus.app.repository.usuario.PacienteRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.MedicoRepository;
import br.com.SGHSS.VidaPlus.app.service.RecepcionistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/recepcionistas")
public class RecepcionistaController {

    @Autowired private RecepcionistaService recepcionistaService;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<Recepcionista> salvar(@RequestBody Recepcionista r) {
        return ResponseEntity.ok(recepcionistaService.salvar(r));
    }

    @PostMapping("/pacientes")
    public ResponseEntity<Paciente> cadastrarPaciente(@RequestBody Paciente p) {
        return ResponseEntity.ok(recepcionistaService.cadastrarPaciente(p));
    }

    @PostMapping("/consultas/presencial")
    public ResponseEntity<ConsultaPresencial> agendarConsultaPresencial(
            @RequestParam Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam String sala,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {

        // Aqui mantemos o fluxo simples do Service
        return ResponseEntity.ok(
                recepcionistaService.agendarConsulta(pacienteId, medicoId, dataHora, sala)
        );
    }
}