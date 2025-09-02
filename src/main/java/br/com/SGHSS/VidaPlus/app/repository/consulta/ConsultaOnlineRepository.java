package br.com.SGHSS.VidaPlus.app.repository.consulta;

import br.com.SGHSS.VidaPlus.app.model.consulta.ConsultaOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaOnlineRepository extends JpaRepository<ConsultaOnline, Long> {
}