package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.MetaAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaAnalysisRepository extends JpaRepository<MetaAnalysis, Long> {
}
