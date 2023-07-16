package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Study;
import org.ensembl.importer.entities.Symptom;
import org.ensembl.importer.repositories.StudyRepository;
import org.ensembl.importer.repositories.UserRepository;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class StudyControllerTest extends TestHelper {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserController userController;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private StudyRepository studyRepository;
    private StudyController studyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        studyController = new StudyController(userRepository, restTemplate, userController,studyRepository);
    }

    @Test
    void getAllStudies() {
        Study[] studies = {study()};
        ResponseEntity<Study[]> responseEntity = new ResponseEntity<>(studies, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<Study> studyList = studyController.getAllStudies();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<Study> expectedUsersList = Arrays.asList(studies);
        assertEquals(expectedUsersList, studyList);

    }

    @Test
    void processStudyChanges() {
        List<Study> studiesFromDB = createStudiesFromDB();
        List<Study> studies = createPatientsList();
        Map<Long, Long> patientsMap = createStudiesMap();

        // Mock behavior of external service call
        String apiUrl = "http://localhost:8082/api/studies";
        Study[] patientsArray = studies.toArray(new Study[0]);
        ResponseEntity<Study[]> responseEntity = new ResponseEntity<>(patientsArray, HttpStatus.OK);
        when(restTemplate.getForEntity(apiUrl, Study[].class)).thenReturn(responseEntity);

        // Mock behavior of other controller methods
        when(studyRepository.findAll()).thenReturn(studiesFromDB);
        when(userController.processUsersChanges()).thenReturn(createUserMap());

        // Mock repository methods
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user())); // <-- Mock with a valid FileUpload
        when(studyRepository.save(any(Study.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Set the fileUploadController in the patientController

        // Invoke the method
        Map<Long, Long> actualPatientsMap = studyController.processStudyChanges();

        // Verify external service call
        verify(restTemplate, times(1)).getForEntity(apiUrl, Study[].class);

        // Verify other controller methods
        verify(userController, times(0)).processUsersChanges();

        // Verify repository methods
//        verify(studyRepository, times(studies.size())).findAll();
//        verify(studyRepository, times(studies.size())).save(any(Study.class));

        // Verify no more interactions
//        verifyNoMoreInteractions(restTemplate, userController, studyRepository);

        // Assert the result
        assertEquals(patientsMap, actualPatientsMap);
    }


    private List<Study> createStudiesFromDB() {
        return Arrays.asList(study2());
    }

    private List<Study> createPatientsList() {
        return Arrays.asList(study());
    }


}