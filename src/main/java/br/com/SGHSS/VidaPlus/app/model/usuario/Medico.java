package br.com.SGHSS.VidaPlus.app.model.usuario;


import br.com.SGHSS.VidaPlus.app.model.IAgendavel;
import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "medicos")
public class Medico extends Usuario implements IAgendavel {

    private String crm;
    private String especialidade;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Consulta> consultas = new ArrayList<>();

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Prontuario> prontuarios = new ArrayList<>();

    @Override
    public void agendar(LocalDateTime dataHora) { /* não usado diretamente */ }

    @Override
    public void cancelarAgendamento() { /* não usado diretamente */ }
}