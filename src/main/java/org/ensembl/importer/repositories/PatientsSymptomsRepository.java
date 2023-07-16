package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.PatientsSymptoms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientsSymptomsRepository extends JpaRepository<PatientsSymptoms, Long> {
    List<PatientsSymptoms> findByPatientId(Long id);
}
