package br.com.SGHSS.VidaPlus.app.service;

import br.com.SGHSS.VidaPlus.app.model.homecare.HomeCare;
import br.com.SGHSS.VidaPlus.app.repository.homecare.HomeCareRepository;
import br.com.SGHSS.VidaPlus.app.service.AuditService;
import br.com.SGHSS.VidaPlus.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HomeCareService {

    @Autowired private HomeCareRepository homeCareRepository;
    @Autowired private NotificationService notificationService;
    @Autowired private AuditService auditService;

    public HomeCare agendar(Long pacienteId, Long enfermeiroId, LocalDateTime dataHora, String procedimentos) {
        HomeCare hc = new HomeCare();
        hc.setDataHora(dataHora);
        hc.setProcedimentos(procedimentos);
        HomeCare salvo = homeCareRepository.save(hc);

        notificationService.notificarPaciente(pacienteId, "Visita HomeCare agendada para " + dataHora);
        auditService.registrarAcao("AGENDAR_HOMECARE", "HomeCareId=" + salvo.getId());
        return salvo;
    }

    public HomeCare registrarVisita(Long homeCareId) {
        HomeCare hc = homeCareRepository.findById(homeCareId)
                .orElseThrow(() -> new IllegalArgumentException("HomeCare não encontrado: " + homeCareId));
        // Aqui você poderia anexar um carimbo de data/hora, status concluído, etc.
        auditService.registrarAcao("REGISTRAR_VISITA_HOMECARE", "HomeCareId=" + homeCareId);
        return homeCareRepository.save(hc);
    }

    public void cancelar(Long homeCareId) {
        HomeCare hc = homeCareRepository.findById(homeCareId)
                .orElseThrow(() -> new IllegalArgumentException("HomeCare não encontrado: " + homeCareId));
        hc.cancelarAgendamento();
        homeCareRepository.save(hc);
        auditService.registrarAcao("CANCELAR_HOMECARE", "HomeCareId=" + homeCareId);
    }
}
