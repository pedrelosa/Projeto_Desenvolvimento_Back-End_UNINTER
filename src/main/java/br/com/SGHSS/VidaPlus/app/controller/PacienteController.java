package br.com.SGHSS.VidaPlus.app.controller;

import br.com.SGHSS.VidaPlus.app.model.clinica.Clinica;
import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaOnline;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.hospital.Hospital;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import br.com.SGHSS.VidaPlus.app.repository.clinica.ClinicaRepository;
import br.com.SGHSS.VidaPlus.app.repository.hospital.HospitalRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.PacienteRepository;
import br.com.SGHSS.VidaPlus.app.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired private PacienteService pacienteService;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private ClinicaRepository clinicaRepository;
    @Autowired private HospitalRepository hospitalRepository;

    @PostMapping
    public ResponseEntity<Paciente> cadastrar(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.cadastrar(paciente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<?>> historico(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.visualizarHistoricoClinico(id));
    }

    // Agendar PRESENCIAL no HOSPITAL
    @PostMapping("/{pacienteId}/consultas/presencial")
    public ResponseEntity<ConsultaPresencial> agendarPresencialNoHospital(
            @PathVariable Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam Long hospitalId,
            @RequestParam String sala,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {

        ConsultaPresencial consulta = pacienteService.agendarConsultaPresencial(pacienteId, medicoId, dataHora, sala);

        // associar consulta ao hospital (lista unidirecional)
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("Hospital não encontrado: " + hospitalId));

        List<ConsultaPresencial> consultas = hospital.getConsultas() == null ? new ArrayList<>() : hospital.getConsultas();
        consultas.add(consulta);
        hospital.setConsultas(consultas);
        hospitalRepository.save(hospital);

        return ResponseEntity.ok(consulta);
    }

    // Agendar ONLINE na CLÍNICA
    @PostMapping("/{pacienteId}/consultas/online")
    public ResponseEntity<ConsultaOnline> agendarOnlineNaClinica(
            @PathVariable Long pacienteId,
            @RequestParam Long medicoId,
            @RequestParam Long clinicaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {

        ConsultaOnline consulta = pacienteService.agendarConsultaOnline(pacienteId, medicoId, dataHora);

        // associar consulta online à clinica? (modelo tem lista de ConsultaPresencial)
        // Mantemos apenas o registro lógico de que a teleconsulta pertence à clínica via negócio;
        // como o modelo não tem lista de online na clínica, nada a persistir aqui.
        Clinica clinica = clinicaRepository.findById(clinicaId)
                .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada: " + clinicaId));
        // (Opcional) Poderíamos registrar algum log/atributo no futuro.

        return ResponseEntity.ok(consulta);
    }

    @PostMapping("/{pacienteId}/teleconsulta/{consultaOnlineId}/acessar")
    public ResponseEntity<String> acessarTeleconsulta(
            @PathVariable Long pacienteId,
            @PathVariable Long consultaOnlineId) {
        // checagens de autorização podem ser aplicadas aqui (perfil/owner)
        String link = pacienteService.acessarTeleconsulta(consultaOnlineId);
        return ResponseEntity.ok(link);
    }
}