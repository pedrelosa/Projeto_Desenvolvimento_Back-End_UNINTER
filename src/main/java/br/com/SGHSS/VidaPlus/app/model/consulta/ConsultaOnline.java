package br.com.SGHSS.VidaPlus.app.model.consulta;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "consultas_online")
public class ConsultaOnline extends Consulta {
    private String linkVideoChamada;
}