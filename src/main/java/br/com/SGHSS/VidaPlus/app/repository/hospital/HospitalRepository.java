package br.com.SGHSS.VidaPlus.app.repository.hospital;

import br.com.SGHSS.VidaPlus.app.model.hospital.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}