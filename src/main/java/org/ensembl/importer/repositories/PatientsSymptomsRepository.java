package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.PatientsSymptoms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientsSymptomsRepository extends JpaRepository<PatientsSymptoms, Long> {
}
