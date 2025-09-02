package br.com.SGHSS.VidaPlus.app.model.consulta;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "consultas_presenciais")
public class ConsultaPresencial extends Consulta {
    private String sala;
}