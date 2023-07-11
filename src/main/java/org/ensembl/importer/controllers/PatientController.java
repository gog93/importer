package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.*;
import org.ensembl.importer.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/patients")
public class PatientController {
    private RestTemplate restTemplate;
    private final PatientRepository patientRepository;
    private final UserController userController;
    private FileUploadController fileUploadController;
    private final StudyController studyController;
    private final FamilyController familyController;
    private final DiseaseController diseaseController;
    private final UserRepository userRepository;
    private final FileUploadRepository fileUploadRepository;
    private final StudyRepository studyRepository;
    private final FamilyRepository familyRepository;
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public PatientController(PatientRepository patientRepository,
                             UserController userController,
                             StudyController studyController,
                             FamilyController familyController,
                             DiseaseController diseaseController,
                             UserRepository userRepository,
                             FileUploadRepository fileUploadRepository,
                             StudyRepository studyRepository,
                             FamilyRepository familyRepository,
                             DiseaseRepository diseaseRepository) {
        this.patientRepository = patientRepository;
        this.userController = userController;
        this.fileUploadController = fileUploadController;
        this.studyController = studyController;
        this.familyController = familyController;
        this.diseaseController = diseaseController;
        this.userRepository = userRepository;
        this.fileUploadRepository = fileUploadRepository;
        this.studyRepository = studyRepository;
        this.familyRepository = familyRepository;
        this.diseaseRepository = diseaseRepository;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        String apiUrl = "http://localhost:8082/api/patients";
        ResponseEntity<Patient[]> response = restTemplate.getForEntity(apiUrl, Patient[].class);
        Patient[] patientsArray = response.getBody();
        List<Patient> patientsList = Arrays.asList(patientsArray);
        return patientsList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processPatientChanges() {
        List<Patient> patientsFromDB = patientRepository.findAll();
        List<Patient> patientsList = getAllPatients();
        Map<Long, Long> patientsMap = createPatientMap(patientsList, patientsFromDB);
        saveNewPatients(patientsList, patientsFromDB, patientsMap);
        return patientsMap;
    }

    private Map<Long, Long> createPatientMap(List<Patient> patientsList, List<Patient> patientsFromDB) {
        Map<Long, Long> patientsMap = new HashMap<>();
        for (Patient patient : patientsList) {
            for (Patient patientFromDB : patientsFromDB) {
                if (patient.getStudy().getPubmedId().equals(patientFromDB.getStudy().getPubmedId()) &&
                        patient.getFamily().getName().equals(patientFromDB.getFamily().getName()) &&
                        patient.getIndividualIdentity().equals(patientFromDB.getIndividualIdentity())) {
                    patientsMap.put(patient.getId(), patientFromDB.getId());
                    break;
                }
            }
        }
        return patientsMap;
    }

    private void saveNewPatients(List<Patient> patientsList, List<Patient> patientsFromDB, Map<Long, Long> patientsMap) {
        for (Patient patient : patientsList) {
            boolean found = false;
            for (Patient patientFromDB : patientsFromDB) {
                if (patient.getStudy().getPubmedId().equals(patientFromDB.getStudy().getPubmedId()) &&
                        patient.getFamily().getName().equals(patientFromDB.getFamily().getName()) &&
                        patient.getIndividualIdentity().equals(patientFromDB.getIndividualIdentity())) {
                    patientsMap.put(patient.getId(), patientFromDB.getId());
                    found = true;
                    break;
                }

            }
            if (!found) {
                Map<Long, Long> studyMap = studyController.processStudyChanges();
                Map<Long, Long> userMap = userController.processUsersChanges();
                Map<Long, Long> familyMap = familyController.processFamilyChanges();
                Map<Long, Long> diseaseMap = diseaseController.processDiseaseChanges();
                Map<Long, Long> fileUploadMap = fileUploadController.processFileUploadChanges();
                Long studyById = studyMap.get(patient.getStudy().getId());
                Long userById = userMap.get(patient.getUpdatedBy().getId());
                Long familyById = familyMap.get(patient.getFamily().getId());
                Long diseaseById = diseaseMap.get(patient.getDisease().getId());
                Long fileUpload = fileUploadMap.get(patient.getFileUpload().getId());
                FileUpload fileUpload1 = fileUploadRepository.findById(fileUpload).get();
                Study byIdStudy = studyRepository.findById(studyById).get();
                Family byIdFamily = familyRepository.findById(familyById).get();
                Disease byIdDisease = diseaseRepository.findById(diseaseById).get();
                User byIdUser = userRepository.findById(userById).get();
                patient.setFileUpload(fileUpload1);
                patient.setStudy(byIdStudy);
                patient.setFamily(byIdFamily);
                patient.setCreatedBy(byIdUser);
                patient.setUpdatedBy(byIdUser);
                patient.setVerifiedBy(byIdUser);
                patient.setDisease(byIdDisease);
                patientRepository.save(patient);
                patientsMap.put(patient.getId(), patient.getId());
            }
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setFileUploadController(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }
}