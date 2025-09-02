package br.com.SGHSS.VidaPlus.app.repository.usuario;


import br.com.SGHSS.VidaPlus.app.model.usuario.Enfermeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnfermeiroRepository extends JpaRepository<Enfermeiro, Long> {
}