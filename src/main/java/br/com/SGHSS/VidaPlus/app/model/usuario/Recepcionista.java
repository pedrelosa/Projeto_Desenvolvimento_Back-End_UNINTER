package br.com.SGHSS.VidaPlus.app.model.usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recepcionistas")
public class Recepcionista extends Usuario {
    private String matricula;
}