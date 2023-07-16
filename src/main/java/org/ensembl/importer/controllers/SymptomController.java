package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.ensembl.importer.entities.Symptom;
import org.ensembl.importer.repositories.SymptomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/symptoms")
public class SymptomController {

    private final SymptomRepository symptomRepository;
    private final RestTemplate restTemplate;


    @GetMapping
    public List<Symptom> getAllSymptoms() {
        // Create a RestTemplate object to make HTTP requests

        // Define the URL of the external API from which to retrieve patient symptom data
        String apiUrl = "http://localhost:8082/api/symptoms";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<Symptom[]> response = restTemplate.getForEntity(apiUrl, Symptom[].class);

        // Extract the array of PatientSymptom objects from the response body
        Symptom[] symptomsArray = response.getBody();

        // Convert the array of PatientSymptom objects to a List
        List<Symptom> symptomsList = Arrays.asList(symptomsArray);

        // Return the list of patient symptoms
        return symptomsList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processSymptomChanges() {
        List<Symptom> symptomsFromDB = symptomRepository.findAll();
        List<Symptom> symptomsList = getAllSymptoms();
        Map<Long, Long> symptomsMap = createSymptomsMap(symptomsList, symptomsFromDB);
        saveNewSymptoms(symptomsList, symptomsFromDB, symptomsMap);
        return symptomsMap;
    }

    private Map<Long, Long> createSymptomsMap(List<Symptom> symptomsList, List<Symptom> symptomFromDB) {
        Map<Long, Long> symptomsMap = new HashMap<>();
        for (Symptom symptoms : symptomsList) {
            for (Symptom symptomsFromDB : symptomFromDB) {
                if (symptoms.getName().equals(symptomsFromDB.getName())) {
                    symptomsMap.put(symptoms.getId(), symptomsFromDB.getId());
                    break;
                }
            }
        }
        return symptomsMap;
    }

    private void saveNewSymptoms(List<Symptom> symptomsList, List<Symptom> symptomsFromDB, Map<Long, Long> symptomssMap) {
        for (Symptom symptom : symptomsList) {
            boolean found = false;
            for (Symptom symptomFromDB : symptomsFromDB) {
                if (symptom.getName().equals(symptomFromDB.getName())) {
                    symptomssMap.put(symptom.getId(), symptomFromDB.getId());
                    found = true;
                    break;
                }

            }
            if (!found) {
                symptomRepository.save(symptom);
                symptomssMap.put(symptom.getId(), symptom.getId());
            }
        }
    }
}
