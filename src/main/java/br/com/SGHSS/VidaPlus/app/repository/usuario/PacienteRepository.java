package br.com.SGHSS.VidaPlus.app.repository.usuario;

import br.com.SGHSS.VidaPlus.app.model.usuario.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}