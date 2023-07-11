package org.ensembl.importer.repositories;

import org.ensembl.importer.entities.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
}
