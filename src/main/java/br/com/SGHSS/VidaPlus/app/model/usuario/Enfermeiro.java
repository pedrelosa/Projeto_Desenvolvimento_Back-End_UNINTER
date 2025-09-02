package br.com.SGHSS.VidaPlus.app.model.usuario;

import br.com.SGHSS.VidaPlus.app.model.homecare.HomeCare;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "enfermeiros")
public class Enfermeiro extends Usuario {

    private String coren;

    @OneToMany(mappedBy = "enfermeiro", cascade = CascadeType.ALL)
    private List<HomeCare> atendimentosHomeCare;
}