package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
