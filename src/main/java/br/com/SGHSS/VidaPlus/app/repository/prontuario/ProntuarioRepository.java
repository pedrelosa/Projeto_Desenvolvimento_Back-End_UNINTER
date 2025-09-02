package br.com.SGHSS.VidaPlus.app.repository.prontuario;

import br.com.SGHSS.VidaPlus.app.model.prontuario.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
}