package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.hospital.Hospital;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.repository.hospital.HospitalRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private AuditService auditService;

    public Hospital salvar(Hospital h) {
        Hospital salvo = hospitalRepository.save(h);
        auditService.registrarAcao("SALVAR_HOSPITAL", "HospitalId=" + salvo.getId());
        return salvo;
    }

    public Hospital buscarPorId(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital não encontrado: " + id));
    }

    public Hospital adicionarMedico(Long hospitalId, Medico medico) {
        Hospital h = buscarPorId(hospitalId);
        List<Medico> medicos = h.getMedicos();
        medicos.add(medico);
        h.setMedicos(medicos);
        Hospital salvo = hospitalRepository.save(h);
        auditService.registrarAcao("ADICIONAR_MEDICO_HOSPITAL", "HospitalId=" + hospitalId);
        return salvo;
    }

    public List<ConsultaPresencial> listarConsultas(Long hospitalId) {
        return buscarPorId(hospitalId).getConsultas();
    }

    public void alocarLeito(Long hospitalId, Long pacienteId) {
        // TODO: implementar após modelarmos Leito.
        throw new UnsupportedOperationException("Funcionalidade de leitos pendente de modelagem.");
    }

    public void liberarLeito(Long hospitalId, Long leitoId) {
        // TODO: implementar após modelarmos Leito.
        throw new UnsupportedOperationException("Funcionalidade de leitos pendente de modelagem.");
    }
}