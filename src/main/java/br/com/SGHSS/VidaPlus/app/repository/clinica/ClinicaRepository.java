package br.com.SGHSS.VidaPlus.app.repository.clinica;

import br.com.SGHSS.VidaPlus.app.model.clinica.Clinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
}
