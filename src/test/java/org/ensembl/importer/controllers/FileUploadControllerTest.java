package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.FileUpload;
import org.ensembl.importer.repositories.FileUploadRepository;
import org.ensembl.importer.repositories.StudyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FileUploadControllerTest extends TestHelper{
    @Mock
    private  FileUploadRepository fileUploadRepository;
    @Mock
    private  StudyController studyController;
    @Mock
    private  StudyRepository studyRepository;
    @Mock
    private  RestTemplate restTemplate;
private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fileUploadController = new FileUploadController(fileUploadRepository, studyController, studyRepository, restTemplate);
    }
    @Test
    void getAllFileUploads() {
       FileUpload[] fileUploads = {fileUpload()};
        ResponseEntity<FileUpload[]> responseEntity = new ResponseEntity<>(fileUploads, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<FileUpload> allFileUploads = fileUploadController.getAllFileUploads();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<FileUpload> uploads = Arrays.asList(fileUploads);
        assertEquals(uploads, allFileUploads);
    }

    @Test
    void processFileUploadChanges() {
    }
}