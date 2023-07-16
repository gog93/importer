package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.PatientsSymptoms;
import org.ensembl.importer.entities.Symptom;
import org.ensembl.importer.repositories.PatientRepository;
import org.ensembl.importer.repositories.PatientsSymptomsRepository;
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

class PatientsSymptomsControllerTest extends TestHelper {
    @Mock
    private PatientController patientController;
    @Mock
    private SymptomController symptomController;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private SymptomRepository symptomRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private PatientsSymptomsRepository patientSymptomRepository;
    private PatientsSymptomsController patientsSymptomsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        patientsSymptomsController = new PatientsSymptomsController(patientController,symptomController,
                patientRepository,symptomRepository,restTemplate,patientSymptomRepository);
    }

    @Test
    void getAllPatientSymptoms() {
        PatientsSymptoms[] symptomsArray = {patientsSymptoms()};
        ResponseEntity<PatientsSymptoms[]> responseEntity = new ResponseEntity<>(symptomsArray, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<PatientsSymptoms> symptomsList = patientsSymptomsController.getAllPatientSymptoms();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<PatientsSymptoms> expectedUsersList = Arrays.asList(symptomsArray);
        assertEquals(expectedUsersList, symptomsList);

    }

    @Test
    void processPatientChanges() {
    }
}