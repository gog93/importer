package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomRepository extends JpaRepository<Symptom,Long> {
}
