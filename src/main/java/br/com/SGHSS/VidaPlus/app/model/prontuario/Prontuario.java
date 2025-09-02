package br.com.SGHSS.VidaPlus.app.model.prontuario;

import br.com.SGHSS.VidaPlus.app.model.usuario.Medico;
import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "prontuarios")
public class Prontuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    private String diagnostico;
    private String prescricoes;
    private LocalDateTime dataCriacao;
}