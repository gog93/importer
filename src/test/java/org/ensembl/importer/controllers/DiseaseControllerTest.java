package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Disease;
import org.ensembl.importer.entities.Patient;
import org.ensembl.importer.repositories.DiseaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DiseaseControllerTest extends TestHelper {
    @Mock
    private DiseaseRepository diseaseRepository;
    @Mock
    private RestTemplate restTemplate;

    private DiseaseController diseaseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        diseaseController = new DiseaseController(diseaseRepository);
    }

    @Test
    void getAllDiseases_ShouldReturnDiseasesList() {
        // Create a mock RestTemplate
        RestTemplate restTemplate = mock(RestTemplate.class);

        // Define the expected response
        Disease[] diseasesArray = {disease()};
        ResponseEntity<Disease[]> responseEntity = new ResponseEntity<>(diseasesArray, HttpStatus.OK);

        // Mock the RestTemplate behavior
        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        // Set the mock RestTemplate in the diseaseController

        // Invoke the method
        List<Disease> diseasesList = diseaseController.getAllDiseases();

        // Verify the RestTemplate behavior
        verify(restTemplate, times(1)).getForEntity(anyString(), any(Class.class));

        // Verify the result
        List<Disease> expectedDiseasesList = Arrays.asList(diseasesArray);
        assertEquals(expectedDiseasesList, diseasesList);
    }

    @Test
    void processDiseaseChanges_ShouldReturnDiseaseMap() {
        List<Disease> patientsFromDB = Arrays.asList(disease());
        List<Disease> patientsList = Arrays.asList(disease2());
        Map<Long, Long> diseasesMap = createDiseaseMap();

        // Mock behavior of external service call
        String apiUrl = "http://localhost:8082/api/diseases";
        Disease[] patientsArray = patientsList.toArray(new Disease[0]);
        ResponseEntity<Disease[]> responseEntity = new ResponseEntity<>(patientsArray, HttpStatus.OK);
        when(restTemplate.getForEntity(apiUrl, Disease[].class)).thenReturn(responseEntity);

        // Mock behavior of other controller methods
        when(diseaseRepository.findAll()).thenReturn(patientsFromDB);
        // Mock repository methods
        when(diseaseRepository.findById(anyLong())).thenReturn(Optional.of(disease())); // <-- Mock with a valid FileUpload
        when(diseaseRepository.findById(anyLong())).thenReturn(Optional.of(disease())); // <-- Mock with a valid FileUpload
        when(diseaseRepository.save(any(Disease.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(diseaseController.processDiseaseChanges()).thenReturn(createDiseaseMap());


        // Invoke the method
        Map<Long, Long> actualPatientsMap = diseaseController.processDiseaseChanges();

        // Verify external service call
        verify(restTemplate, times(1)).getForEntity(apiUrl, Patient[].class);

        // Verify other controller methods
        verify(diseaseController, times(1)).processDiseaseChanges();

        // Verify repository methods
        verify(diseaseRepository, times(patientsList.size())).findAll();

        // Verify no more interactions
        verifyNoMoreInteractions(
                restTemplate,
                diseaseController);

        // Assert the result
        assertEquals(diseasesMap, actualPatientsMap);
    }
}
