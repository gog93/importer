package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.SequenceVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SequenceVariationRepository extends JpaRepository<SequenceVariation, Long> {


    List<SequenceVariation> findByGeneName(String classNumber);
}
