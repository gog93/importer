package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.MetaStudyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaStudyRelationRepository extends JpaRepository<MetaStudyRelation, Long> {
}
