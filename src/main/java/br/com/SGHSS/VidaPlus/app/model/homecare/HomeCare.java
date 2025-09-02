package br.com.SGHSS.VidaPlus.app.model.homecare;

import br.com.SGHSS.VidaPlus.app.model.IAgendavel;
import br.com.SGHSS.VidaPlus.app.model.usuario.Enfermeiro;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "home_care")
public class HomeCare implements IAgendavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "enfermeiro_id")
    private Enfermeiro enfermeiro;

    private LocalDateTime dataHora;
    private String procedimentos;

    @Override
    public void agendar(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public void cancelarAgendamento() {
        this.dataHora = null;
    }
}