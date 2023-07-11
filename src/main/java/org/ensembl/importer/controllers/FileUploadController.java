package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.FileUpload;
import org.ensembl.importer.entities.Study;
import org.ensembl.importer.repositories.FileUploadRepository;
import org.ensembl.importer.repositories.StudyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fileUpload")

public class FileUploadController {
    private final FileUploadRepository fileUploadRepository;
    private final StudyController studyController;
    private final StudyRepository studyRepository;

    public FileUploadController(FileUploadRepository fileUploadRepository, StudyController studyController, StudyRepository studyRepository) {
        this.fileUploadRepository = fileUploadRepository;
        this.studyController = studyController;
        this.studyRepository = studyRepository;
    }

    @GetMapping
    public List<FileUpload> getAllFileUploads() {
        // Create a RestTemplate object to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://localhost:8082/api/file-uploads";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<FileUpload[]> response = restTemplate.getForEntity(apiUrl, FileUpload[].class);

        FileUpload[] fileUploadsArray = response.getBody();

        List<FileUpload> fileUploadList = Arrays.asList(fileUploadsArray);

        return fileUploadList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processFileUploadChanges() {
        List<FileUpload> fileUploadFromDB = fileUploadRepository.findAll();

        List<FileUpload> fileUploadList = getAllFileUploads();

        Map<Long, Long> fileUploadMap = createFileUploadMap(fileUploadList, fileUploadFromDB);

        saveNewFileUploads(fileUploadList, fileUploadFromDB, fileUploadMap);

        return fileUploadMap;
    }

    private Map<Long, Long> createFileUploadMap(List<FileUpload> fileUploadList, List<FileUpload> fileUploadsFromDB) {
        Map<Long, Long> fileUploadsMap = new HashMap<>();
        for (FileUpload fileUpload : fileUploadList) {
            for (FileUpload fileUploadFromDB : fileUploadsFromDB) {
                if (fileUpload.getTsvFileFileName().equals(fileUploadFromDB.getTsvFileFileName())) {
                    fileUploadsMap.put(fileUpload.getId(), fileUploadFromDB.getId());
                    break;
                }
            }
        }
        return fileUploadsMap;
    }

    private void saveNewFileUploads(List<FileUpload> fileUploadsList, List<FileUpload> fileUploadsFromDB, Map<Long, Long> fileUploadsMap) {
        for (FileUpload fileUpload : fileUploadsList) {
            boolean found = false;
            for (FileUpload fileUploadFromDB : fileUploadsFromDB) {
                if (fileUpload.getTsvFileFileName().equals(fileUploadFromDB.getTsvFileFileName())) {
                    found = true;
                    fileUploadsMap.put(fileUpload.getId(), fileUploadFromDB.getId());
                    break;
                }
            }
            if (!found) {
                Map<Long, Long> studyMap = studyController.processStudyChanges();
                Long aStudy = studyMap.get(fileUpload.getStudy().getId());
                Study byIdStudy = studyRepository.findById(aStudy).get();
                fileUpload.setStudy(byIdStudy);
                fileUploadRepository.save(fileUpload);
                fileUploadsMap.put(fileUpload.getId(), fileUpload.getId());
            }
        }
    }
}
