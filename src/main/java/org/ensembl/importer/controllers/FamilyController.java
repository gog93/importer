package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Family;
import org.ensembl.importer.entities.MetaAnalysis;
import org.ensembl.importer.entities.Study;
import org.ensembl.importer.repositories.FamilyRepository;
import org.ensembl.importer.repositories.StudyRepository;
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
@RequestMapping("/api/families")
public class FamilyController {

    private final FamilyRepository familyRepository;


    public FamilyController(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;

    }

    @GetMapping
    public List<Family> getAllFamilies() {
        // Create a RestTemplate object to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL of the external API from which to retrieve family data
        String apiUrl = "http://localhost:8082/api/families";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<Family[]> response = restTemplate.getForEntity(apiUrl, Family[].class);

        // Extract the array of Family objects from the response body
        Family[] familiesArray = response.getBody();

        // Convert the array of Family objects to a List
        List<Family> familiesList = Arrays.asList(familiesArray);

        // Return the list of families
        return familiesList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processFamilyChanges() {
        // Read families from the database
        List<Family> familiesFromDB = familyRepository.findAll();

        // Retrieve the list of families from the external API
        List<Family> familiesList = getAllFamilies();

        // Create a map to store the mapping between family IDs from the external API and the database
        Map<Long, Long> familiesMap = createFamilyMap(familiesList, familiesFromDB);

        // Save new families to the database and update the map with corresponding IDs
        saveNewFamilies(familiesList, familiesFromDB, familiesMap);

        // Return the map of family IDs
        return familiesMap;
    }

    private Map<Long, Long> createFamilyMap(List<Family> familiesList, List<Family> familiesFromDB) {
        Map<Long, Long> familiesMap = new HashMap<>();
        for (Family family : familiesList) {
            for (Family familyFromDB : familiesFromDB) {
                if (family.getName().equals(familyFromDB.getName())) {
                    familiesMap.put(family.getId(), familyFromDB.getId());
                    break;
                }
            }
        }
        return familiesMap;
    }

    private void saveNewFamilies(List<Family> familiesList, List<Family> familiesFromDB, Map<Long, Long> familiesMap) {
        for (Family family : familiesList) {
            boolean found = false;
            for (Family familyFromDB : familiesFromDB) {
                if (family.getName().equals(familyFromDB.getName())) {
                    found = true;
                    familiesMap.put(family.getId(), familyFromDB.getId());
                    break;
                }
            }
            if (!found) {

                familyRepository.save(family);
                familiesMap.put(family.getId(), family.getId());
            }
        }
    }
}
