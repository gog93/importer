package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.ensembl.importer.entities.Disease;
import org.ensembl.importer.repositories.DiseaseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@RestController
@RequestMapping("/api/diseases")
public class DiseaseController {

    private final DiseaseRepository diseaseRepository;
//    private final RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping
    public List<Disease> getAllDiseases() {

        String apiUrl = "http://localhost:8082/api/diseases";
        RestTemplate restTemplate = restTemplate();
        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<Disease[]> response = restTemplate.getForEntity(apiUrl, Disease[].class);

        // Extract the array of Disease objects from the response body
        Disease[] diseasesArray = response.getBody();

        // Convert the array of Disease objects to a List
        List<Disease> diseasesList = Arrays.asList(diseasesArray);

        // Return the list of diseases
        return diseasesList;
    }
    public String checkTwoTables() {
        List<Disease> diseasesFromDB = diseaseRepository.findAll();

        // Retrieve the list of diseases from the external API
        List<Disease> diseasesList = getAllDiseases();
        if (!diseasesFromDB.containsAll(diseasesList)) {
            processDiseaseChanges();
            return "uncheck";
        }
        return "checked";
    }
    @GetMapping("/process-changes")
    public Map<Long, Long> processDiseaseChanges() {
        // Read diseases from the database
        List<Disease> diseasesFromDB = diseaseRepository.findAll();

        // Retrieve the list of diseases from the external API
        List<Disease> diseasesList = getAllDiseases();

        // Create a map to store the mapping between disease IDs from the external API and the database
        Map<Long, Long> diseasesMap = createDiseaseMap(diseasesList, diseasesFromDB);

        // Save new diseases to the database and update the map with corresponding IDs
        saveNewDiseases(diseasesList, diseasesFromDB, diseasesMap);

        // Return the map of disease IDs
        return diseasesMap;
    }

    private Map<Long, Long> createDiseaseMap(List<Disease> diseasesList, List<Disease> diseasesFromDB) {
        Map<Long, Long> diseasesMap = new HashMap<>();
        for (Disease disease : diseasesList) {
            for (Disease diseaseFromDB : diseasesFromDB) {
                if (disease.getName().equals(diseaseFromDB.getName())) {
                    diseasesMap.put(disease.getId(), diseaseFromDB.getId());
                    break;
                }
            }
        }
        return diseasesMap;
    }

    private void saveNewDiseases(List<Disease> diseasesList, List<Disease> diseasesFromDB, Map<Long, Long> diseasesMap) {
        for (Disease disease : diseasesList) {
            boolean found = false;
            for (Disease diseaseFromDB : diseasesFromDB) {
                if (disease.getName().equals(diseaseFromDB.getName())) {
                    found = true;
                    diseasesMap.put(disease.getId(), diseaseFromDB.getId());
                    break;
                }
            }
            if (!found) {
                Long parentId = diseasesMap.get(disease.getParent().getId());
                Disease parentDisease = diseaseRepository.findById(parentId).orElse(null);
                if (parentDisease != null) {
                    disease.setParent(parentDisease);
                    disease.setUpdatedAt(LocalDateTime.now());
                    diseaseRepository.save(disease);
                    diseasesMap.put(disease.getId(), disease.getId());
                }
            }
        }
    }
}
