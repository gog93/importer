package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Symptom;
import org.ensembl.importer.entities.User;
import org.ensembl.importer.repositories.SymptomRepository;
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

class SymptomControllerTest extends TestHelper {
    @Mock
    private SymptomRepository symptomRepository;
    @Mock
    private RestTemplate restTemplate;
    private SymptomController symptomController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        symptomController = new SymptomController(symptomRepository, restTemplate);
    }

    @Test
    void getAllSymptoms() {
        Symptom[] symptomsArray = {symptom()};
        ResponseEntity<Symptom[]> responseEntity = new ResponseEntity<>(symptomsArray, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<Symptom> symptomsList = symptomController.getAllSymptoms();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<Symptom> expectedUsersList = Arrays.asList(symptomsArray);
        assertEquals(expectedUsersList, symptomsList);
    }

    @Test
    void processSymptomChanges() {
    }
}