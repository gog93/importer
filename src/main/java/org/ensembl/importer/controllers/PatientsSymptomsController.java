package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Patient;
import org.ensembl.importer.entities.PatientsSymptoms;
import org.ensembl.importer.entities.Symptom;
import org.ensembl.importer.repositories.PatientRepository;
import org.ensembl.importer.repositories.PatientsSymptomsRepository;
import org.ensembl.importer.repositories.SymptomRepository;
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
@RequestMapping("/api/patients-symptoms")
public class PatientsSymptomsController {
    private final PatientController patientController;
    private final SymptomController symptomController;
    private final PatientRepository patientRepository;
    private final SymptomRepository symptomRepository;

    private final PatientsSymptomsRepository patientSymptomRepository;

    @Autowired
    public PatientsSymptomsController(PatientController patientController,
                                      SymptomController symptomController,
                                      PatientRepository patientRepository,
                                      SymptomRepository symptomRepository,
                                      PatientsSymptomsRepository patientSymptomRepository) {
        this.patientController = patientController;
        this.symptomController = symptomController;
        this.patientRepository = patientRepository;
        this.symptomRepository = symptomRepository;
        this.patientSymptomRepository = patientSymptomRepository;
    }

    @GetMapping
    public List<PatientsSymptoms> getAllPatientSymptoms() {
        // Create a RestTemplate object to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL of the external API from which to retrieve patient symptom data
        String apiUrl = "http://localhost:8082/api/patients-symptoms";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<PatientsSymptoms[]> response = restTemplate.getForEntity(apiUrl, PatientsSymptoms[].class);

        // Extract the array of PatientSymptom objects from the response body
        PatientsSymptoms[] patientSymptomsArray = response.getBody();

        // Convert the array of PatientSymptom objects to a List
        List<PatientsSymptoms> patientSymptomsList = Arrays.asList(patientSymptomsArray);

        // Return the list of patient symptoms
        return patientSymptomsList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processPatientChanges() {
        List<PatientsSymptoms> patientsSymptomsFromDB = patientSymptomRepository.findAll();
        List<PatientsSymptoms> patientsSymptomsList = getAllPatientSymptoms();
        Map<Long, Long> patientsSymptomsMap = createPatientsSymptomsMap(patientsSymptomsList, patientsSymptomsFromDB);
        saveNewPatientsSymptoms(patientsSymptomsList, patientsSymptomsFromDB, patientsSymptomsMap);
        return patientsSymptomsMap;
    }

    private Map<Long, Long> createPatientsSymptomsMap(List<PatientsSymptoms> patientsSymptomsList, List<PatientsSymptoms> patientsSymptomFromDB) {
        Map<Long, Long> patientsSymptomsMap = new HashMap<>();
        for (PatientsSymptoms patientsSymptoms : patientsSymptomsList) {
            for (PatientsSymptoms patientsSymptomsFromDB : patientsSymptomFromDB) {
                if (patientsSymptoms.getPatient().getStudy().getPubmedId().equals(patientsSymptomsFromDB.getPatient().getStudy().getPubmedId()) &&
                        patientsSymptoms.getPatient().getFamily().getName().equals(patientsSymptomsFromDB.getPatient().getFamily().getName()) &&
                        patientsSymptoms.getPatient().getIndividualIdentity().equals(patientsSymptomsFromDB.getPatient().getIndividualIdentity())) {
                    patientsSymptomsMap.put(patientsSymptoms.getId(), patientsSymptomsFromDB.getId());
                    break;
                }
            }
        }
        return patientsSymptomsMap;
    }

    private void saveNewPatientsSymptoms(List<PatientsSymptoms> patientsSymptomsList, List<PatientsSymptoms> patientsFromDB, Map<Long, Long> patientsMap) {
        for (PatientsSymptoms patientsSymptoms : patientsSymptomsList) {
            boolean found = false;
            for (PatientsSymptoms patientsSymptomsFromDB : patientsFromDB) {
                if (patientsSymptoms.getPatient().getStudy().getPubmedId().equals(patientsSymptomsFromDB.getPatient().getStudy().getPubmedId()) &&
                        patientsSymptoms.getPatient().getFamily().getName().equals(patientsSymptomsFromDB.getPatient().getFamily().getName()) &&
                        patientsSymptoms.getPatient().getIndividualIdentity().equals(patientsSymptomsFromDB.getPatient().getIndividualIdentity())) {
                    patientsMap.put(patientsSymptoms.getId(), patientsSymptomsFromDB.getId());
                    found = true;
                    break;
                }

            }
            if (!found) {
                Map<Long, Long> symptomMap = symptomController.processSymptomChanges();
                Map<Long, Long> patientMap = patientController.processPatientChanges();
                Long id = symptomMap.get(patientsSymptoms.getSymptom().getId());
                Long idByPatient = patientMap.get(patientsSymptoms.getPatient().getId());
                Patient byId = patientRepository.findById(idByPatient).get();
                Symptom byIdSymptom = symptomRepository.findById(id).get();
                patientsSymptoms.setPatient(byId);
                patientsSymptoms.setSymptom(byIdSymptom);
                patientSymptomRepository.save(patientsSymptoms);
                patientsMap.put(patientsSymptoms.getId(), patientsSymptoms.getId());
            }
        }
    }
}
