package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.clinica.Clinica;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.repository.clinica.ClinicaRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicaService {

    @Autowired private ClinicaRepository clinicaRepository;
    @Autowired private AuditService auditService;

    public Clinica salvar(Clinica c) {
        Clinica salvo = clinicaRepository.save(c);
        auditService.registrarAcao("SALVAR_CLINICA", "ClinicaId=" + salvo.getId());
        return salvo;
    }

    public Clinica buscarPorId(Long id) {
        return clinicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada: " + id));
    }

    // Unidirecional: adiciona listas internas
    public Clinica adicionarMedico(Long clinicaId, Medico medico) {
        Clinica c = buscarPorId(clinicaId);
        List<Medico> medicos = c.getMedicos();
        medicos.add(medico);
        c.setMedicos(medicos);
        Clinica salvo = clinicaRepository.save(c);
        auditService.registrarAcao("ADICIONAR_MEDICO_CLINICA", "ClinicaId=" + clinicaId);
        return salvo;
    }

    public List<ConsultaPresencial> listarConsultas(Long clinicaId) {
        return buscarPorId(clinicaId).getConsultas();
    }
}
