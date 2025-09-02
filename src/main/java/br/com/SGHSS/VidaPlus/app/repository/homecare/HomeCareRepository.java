package br.com.SGHSS.VidaPlus.app.repository.homecare;

import br.com.SGHSS.VidaPlus.app.model.homecare.HomeCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeCareRepository extends JpaRepository<HomeCare, Long> {
}
