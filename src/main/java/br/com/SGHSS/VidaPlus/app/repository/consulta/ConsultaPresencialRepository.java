package br.com.SGHSS.VidaPlus.app.repository.consulta;

import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaPresencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaPresencialRepository extends JpaRepository<ConsultaPresencial, Long> {
}