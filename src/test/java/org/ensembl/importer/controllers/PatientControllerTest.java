package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Patient;
import org.ensembl.importer.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PatientControllerTest extends TestHelper {
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private UserController userController;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private FileUploadController fileUploadController;
    @Mock
    private StudyController studyController;
    @Mock
    private FamilyController familyController;
    @Mock
    private DiseaseController diseaseController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FileUploadRepository fileUploadRepository;
    @Mock
    private StudyRepository studyRepository;
    @Mock
    private FamilyRepository familyRepository;
    @Mock
    private DiseaseRepository diseaseRepository;

    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        patientController = new PatientController(restTemplate, patientRepository, userController, fileUploadController, studyController, familyController,
                diseaseController, userRepository, fileUploadRepository, studyRepository, familyRepository, diseaseRepository);
    }


    @Test
    void getAllPatients_ShouldReturnListOfPatients() {
        Patient[] symptomsArray = {patient()};
        ResponseEntity<Patient[]> responseEntity = new ResponseEntity<>(symptomsArray, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<Patient> symptomsList = patientController.getAllPatients();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<Patient> expectedUsersList = Arrays.asList(symptomsArray);
        assertEquals(expectedUsersList, symptomsList);
    }

    @Test
    void processPatientChanges_ShouldReturnPatientsMap() {

    }


    private List<Patient> createPatientsList() {
        return Arrays.asList(patient());
    }

    private List<Patient> createPatientsFromDB() {
        // Create and return a list of patients from the database
        // Return an empty list or some predefined patients as per your test case
        return Arrays.asList(patient2());
    }


}

