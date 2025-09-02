package br.com.SGHSS.VidaPlus.app.repository.usuario;

import br.com.SGHSS.VidaPlus.app.model.usuario.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long> {
}