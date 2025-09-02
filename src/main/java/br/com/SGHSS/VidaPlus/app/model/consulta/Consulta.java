package br.com.SGHSS.VidaPlus.app.model.consulta;

import br.com.SGHSS.VidaPlus.app.model.IAgendavel;
import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "consultas")
public abstract class Consulta implements IAgendavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    private LocalDateTime dataHora;
    private String status; // Sugestão: migrar para enum futuramente

    @Override
    public void agendar(LocalDateTime dataHora) {
        this.dataHora = dataHora;
        if (this.status == null) this.status = "Agendada";
    }

    @Override
    public void cancelarAgendamento() {
        this.status = "Cancelada";
    }
}