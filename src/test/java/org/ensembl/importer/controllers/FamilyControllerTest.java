package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Family;
import org.ensembl.importer.entities.FileUpload;
import org.ensembl.importer.repositories.FamilyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FamilyControllerTest extends TestHelper{
    @Mock
    private  FamilyRepository familyRepository;
    @Mock
    private  RestTemplate restTemplate;
    private FamilyController familyController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        familyController = new FamilyController(familyRepository, restTemplate);
    }


    @Test
    void getAllFamilies() {
        Family[] families = {family()};
        ResponseEntity<Family[]> responseEntity = new ResponseEntity<>(families, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<Family> allFamilies = familyController.getAllFamilies();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<Family> uploads = Arrays.asList(families);
        assertEquals(uploads, allFamilies);
    }

    @Test
    void processFamilyChanges() {
    }
}