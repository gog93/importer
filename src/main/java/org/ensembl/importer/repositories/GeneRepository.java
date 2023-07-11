package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.Gene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneRepository extends JpaRepository<Gene, Integer> {

    // Define your custom queries here if needed

}
