package br.com.SGHSS.VidaPlus.app.repository.gerencia;

import br.com.SGHSS.VidaPlus.app.model.gerencia.Gerencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GerenciaRepository extends JpaRepository<Gerencia, Long> {
}