package br.com.SGHSS.VidaPlus.app.testsupport;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaOnline;
import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaOnlineRepository;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaPresencialRepository;
import br.com.SGHSS.VidaPlus.app.repository.consulta.ConsultaRepository;
import br.com.SGHSS.VidaPlus.app.repository.prontuario.ProntuarioRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.MedicoRepository;
import br.com.SGHSS.VidaPlus.app.repository.usuario.PacienteRepository;
import br.com.SGHSS.VidaPlus.app.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock private PacienteRepository pacienteRepository;
    @Mock private MedicoRepository medicoRepository;
    @Mock private ConsultaRepository consultaRepository;
    @Mock private ConsultaPresencialRepository consultaPresencialRepository;
    @Mock private ConsultaOnlineRepository consultaOnlineRepository;
    @Mock private ProntuarioRepository prontuarioRepository;

    @Mock private NotificationService notificationService;
    @Mock private AuditService auditService;
    @Mock private TelemedicinaService telemedicinaService;

    @InjectMocks private PacienteService pacienteService;

    // Também testaremos confirmar/cancelar via ConsultaService (unitariamente)
    @Mock private ConsultaRepository consultaRepoForConsultaService;
    @Mock private AuditService auditServiceForConsultaService;
    private ConsultaService consultaService;

    private Paciente paciente;
    private Medico medico;

    @BeforeEach
    void setup() {
        consultaService = new ConsultaService();
        // Injetando dependências "manualmente" (padrão simples para teste unitário)
        // Usando reflexão por ser campo package-private @Autowired no serviço real
        try {
            var f1 = ConsultaService.class.getDeclaredField("consultaRepository");
            f1.setAccessible(true);
            f1.set(consultaService, consultaRepoForConsultaService);
            var f2 = ConsultaService.class.getDeclaredField("auditService");
            f2.setAccessible(true);
            f2.set(consultaService, auditServiceForConsultaService);
        } catch (Exception e) { throw new RuntimeException(e); }

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João Paciente");

        medico = new Medico();
        medico.setId(10L);
        medico.setNome("Dra. Ana Cardio");
        medico.setCrm("CRM-123");
        medico.setEspecialidade("Cardiologia");

        given(pacienteRepository.save(any(Paciente.class))).willAnswer(inv -> {
            Paciente p = inv.getArgument(0);
            if (p.getId() == null) p.setId(1L);
            return p;
        });
        given(medicoRepository.findById(10L)).willReturn(Optional.of(medico));
        given(pacienteRepository.findById(1L)).willReturn(Optional.of(paciente));

        // Mocks de save
        given(consultaPresencialRepository.save(any(ConsultaPresencial.class)))
                .willAnswer(inv -> {
                    ConsultaPresencial c = inv.getArgument(0);
                    if (c.getId() == null) c.setId(100L);
                    return c;
                });
        given(consultaOnlineRepository.save(any(ConsultaOnline.class)))
                .willAnswer(inv -> {
                    ConsultaOnline c = inv.getArgument(0);
                    if (c.getId() == null) c.setId(200L);
                    return c;
                });
        given(consultaRepository.findAll()).willReturn(List.of());
    }

    @Test
    @DisplayName("Cenário 1: Consulta presencial em HOSPITAL – agendar e confirmar")
    void consultaPresencialEmHospital() {
        // Arrange
        Paciente salvo = pacienteService.cadastrar(paciente);
        LocalDateTime quando = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);

        // Act – agenda
        ConsultaPresencial agendada = pacienteService.agendarConsultaPresencial(
                salvo.getId(), medico.getId(), quando, "Sala 101");

        // Confirmar via ConsultaService
        // Mock do findById para ConsultaService
        given(consultaRepoForConsultaService.findById(agendada.getId()))
                .willReturn(Optional.of(agendada));

        Consulta confirmada = consultaService.confirmar(agendada.getId());

        // Assert
        assertThat(agendada.getStatus()).isEqualTo("Agendada");
        assertThat(agendada.getSala()).isEqualTo("Sala 101");
        assertThat(confirmada.getStatus()).isEqualTo("Confirmada");
        verify(notificationService).notificarPaciente(eq(salvo.getId()), contains("Consulta presencial agendada"));
        verify(notificationService).notificarMedico(eq(medico.getId()), contains("Nova consulta presencial"));
        verify(auditService, atLeastOnce()).registrarAcao(eq("AGENDAR_CONSULTA_PRESENCIAL"), anyString());
        verify(auditServiceForConsultaService).registrarAcao(eq("CONFIRMAR_CONSULTA"), contains("ConsultaId="));
    }

    @Test
    @DisplayName("Cenário 2: Consulta ONLINE em CLÍNICA – agendar e acessar link seguro")
    void consultaOnlineEmClinica() {
        // Arrange
        Paciente salvo = pacienteService.cadastrar(paciente);
        LocalDateTime quando = LocalDateTime.now().plusDays(2).withHour(14).withMinute(0);

        // Act – agenda e acessa link
        ConsultaOnline online = pacienteService.agendarConsultaOnline(salvo.getId(), medico.getId(), quando);
        given(telemedicinaService.gerarLinkSeguroTeleconsulta(online.getId()))
                .willReturn("https://telemed.vidaplus/consulta/" + online.getId());
        String link = pacienteService.acessarTeleconsulta(online.getId());

        // Assert
        assertThat(online.getStatus()).isEqualTo("Agendada");
        assertThat(online.getDataHora()).isEqualTo(quando);
        assertThat(link).contains("https://telemed.vidaplus/consulta/");
        verify(notificationService).notificarPaciente(eq(salvo.getId()), contains("Consulta online agendada"));
        verify(notificationService).notificarMedico(eq(medico.getId()), contains("Nova consulta online"));
        verify(auditService, atLeastOnce()).registrarAcao(eq("AGENDAR_CONSULTA_ONLINE"), anyString());
        verify(telemedicinaService).gerarLinkSeguroTeleconsulta(online.getId());
        verify(auditService).registrarAcao(eq("ACESSAR_TELECONSULTA"), contains("ConsultaId=" + online.getId()));
    }

    @Test
    @DisplayName("Cancelar consulta – altera status e notifica partes")
    void cancelarConsulta() {
        // Arrange: criar uma consulta genérica
        ConsultaPresencial c = new ConsultaPresencial();
        c.setId(555L);
        c.setPaciente(paciente);
        c.setMedico(medico);
        c.setStatus("Agendada");

        given(consultaRepository.findById(555L)).willReturn(Optional.of(c));
        given(consultaRepository.save(any(Consulta.class))).willAnswer(inv -> inv.getArgument(0));

        // Act
        pacienteService.cancelarConsulta(555L);

        // Assert
        assertThat(c.getStatus()).isEqualTo("Cancelada");
        verify(notificationService).notificarPaciente(eq(paciente.getId()), contains("cancelada"));
        verify(notificationService).notificarMedico(eq(medico.getId()), contains("cancelada"));
        verify(auditService).registrarAcao(eq("CANCELAR_CONSULTA"), contains("ConsultaId=555"));
    }
}