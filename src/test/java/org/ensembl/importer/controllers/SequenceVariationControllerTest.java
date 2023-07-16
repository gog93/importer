package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.SequenceVariation;
import org.ensembl.importer.entities.Study;
import org.ensembl.importer.repositories.GeneRepository;
import org.ensembl.importer.repositories.PatientRepository;
import org.ensembl.importer.repositories.SequenceVariationRepository;
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

class SequenceVariationControllerTest extends TestHelper{
    @Mock
    private  GeneController geneController;
    @Mock
    private  PatientController patientController;
    @Mock
    private  GeneRepository geneRepository;
    @Mock
    private  PatientRepository patientRepository;
    @Mock
    private  SequenceVariationRepository sequenceVariationRepository;
    private SequenceVariationController sequenceVariationController;
    @Mock
    private  RestTemplate restTemplate;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sequenceVariationController = new SequenceVariationController(geneController, patientController,geneRepository,patientRepository,sequenceVariationRepository,restTemplate);
    }
    @Test
    void getAllSequenceVariations() {
        SequenceVariation[] sequenceVariations = {sequenceVariation()};
        ResponseEntity<SequenceVariation[]> responseEntity = new ResponseEntity<>(sequenceVariations, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<SequenceVariation> allSequenceVariationsList = sequenceVariationController.getAllSequenceVariations();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<SequenceVariation> expectedVariationsList = Arrays.asList(sequenceVariations);
        assertEquals(expectedVariationsList, allSequenceVariationsList);
    }

    @Test
    void processSequenceVariationChanges() {
    }
}