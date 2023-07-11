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

import java.util.*;

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

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//        patientController = new PatientController(patientRepository, userController, studyController, familyController,
//                diseaseController, restTemplate,userRepository, fileUploadRepository, studyRepository, familyRepository, diseaseRepository);
//    }


    @Test
    void getAllPatients_ShouldReturnListOfPatients() {
        Patient[] patientsArray = {patient(), patient2()};
        ResponseEntity<Patient[]> responseEntity = new ResponseEntity<>(patientsArray, HttpStatus.OK);
        String apiUrl = "http://localhost:8082/api/patients";
        when(restTemplate.getForEntity(apiUrl, Patient[].class)).thenReturn(responseEntity);


        List<Patient> expectedPatients = Arrays.asList(patientsArray);
        List<Patient> actualPatients = patientController.getAllPatients();

        verify(restTemplate, times(1)).getForEntity(apiUrl, Patient[].class);
        verifyNoMoreInteractions(restTemplate);
        assertEquals(expectedPatients, actualPatients);
    }

    @Test
    void processPatientChanges_ShouldReturnPatientsMap() {
        // Mock data
        List<Patient> patientsFromDB = createPatientsFromDB();
        List<Patient> patientsList = createPatientsList();
        Map<Long, Long> patientsMap = createPatientsMap();

        // Mock behavior of external service call
        String apiUrl = "http://localhost:8082/api/patients";
        Patient[] patientsArray = patientsList.toArray(new Patient[0]);
        ResponseEntity<Patient[]> responseEntity = new ResponseEntity<>(patientsArray, HttpStatus.OK);
        when(restTemplate.getForEntity(apiUrl, Patient[].class)).thenReturn(responseEntity);

        // Mock behavior of other controller methods
        when(patientRepository.findAll()).thenReturn(patientsFromDB);
        when(studyController.processStudyChanges()).thenReturn(createStudyMap());
        when(userController.processUsersChanges()).thenReturn(createUserMap());
        when(familyController.processFamilyChanges()).thenReturn(createFamilyMap());
        when(diseaseController.processDiseaseChanges()).thenReturn(createDiseaseMap());

        // Initialize the fileUploadController mock
        fileUploadController = mock(FileUploadController.class);
        when(fileUploadController.processFileUploadChanges()).thenReturn(createFileUploadMap());

        // Mock repository methods
        when(fileUploadRepository.findById(anyLong())).thenReturn(Optional.of(fileUpload())); // <-- Mock with a valid FileUpload
        when(studyRepository.findById(anyLong())).thenReturn(Optional.of(study())); // <-- Mock with a valid FileUpload
        when(familyRepository.findById(anyLong())).thenReturn(Optional.of(family())); // <-- Mock with a valid FileUpload
        when(diseaseRepository.findById(anyLong())).thenReturn(Optional.of(disease())); // <-- Mock with a valid FileUpload
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user()));// <-- Mock with a valid FileUpload
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Set the fileUploadController in the patientController
        patientController.setFileUploadController(fileUploadController);

        // Invoke the method
        Map<Long, Long> actualPatientsMap = patientController.processPatientChanges();

        // Verify external service call
        verify(restTemplate, times(1)).getForEntity(apiUrl, Patient[].class);

        // Verify other controller methods
        verify(studyController, times(1)).processStudyChanges();
        verify(userController, times(1)).processUsersChanges();
        verify(familyController, times(1)).processFamilyChanges();
        verify(diseaseController, times(1)).processDiseaseChanges();
        verify(fileUploadController, times(1)).processFileUploadChanges();

        // Verify repository methods
        verify(patientRepository, times(patientsList.size())).findAll();
        verify(patientRepository, times(patientsList.size())).save(any(Patient.class));
        verify(fileUploadRepository, times(patientsList.size())).findById(anyLong());

        // Verify no more interactions
        verifyNoMoreInteractions(
                restTemplate, studyController, userController, familyController,
                diseaseController, fileUploadController, patientRepository, fileUploadRepository
        );

        // Assert the result
        assertEquals(patientsMap, actualPatientsMap);
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

