package br.com.SGHSS.VidaPlus.app.model.gerencia;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "gerencia")
public class Gerencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⚠ Removido: relacionamento com Usuario (Usuario é @MappedSuperclass)
    // @OneToMany
    // private List<Usuario> usuarios;

    @OneToMany
    private List<Consulta> consultas;
}
