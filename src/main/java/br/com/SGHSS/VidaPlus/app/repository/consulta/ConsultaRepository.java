package br.com.SGHSS.VidaPlus.app.repository.consulta;

import br.com.SGHSS.VidaPlus.app.model.consulta.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}