package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Study;
import org.ensembl.importer.entities.User;
import org.ensembl.importer.repositories.StudyRepository;
import org.ensembl.importer.repositories.UserRepository;
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
@RequestMapping("/api/studies")
public class StudyController {

    private final UserRepository userRepository;

  private   RestTemplate restTemplate;

    private final UserController userController;
    private final StudyRepository studyRepository;

    @Autowired
    public StudyController(UserRepository userRepository, UserController userController, StudyRepository studyRepository) {
        this.userRepository = userRepository;
        this.userController = userController;
        this.studyRepository = studyRepository;
    }

    @GetMapping
    public List<Study> getAllStudies() {
        // Create a RestTemplate object to make HTTP requests


        // Define the URL of the external API from which to retrieve study data
        String apiUrl = "http://localhost:8082/api/studies";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<Study[]> response = restTemplate.getForEntity(apiUrl, Study[].class);

        // Extract the array of Study objects from the response body
        Study[] studiesArray = response.getBody();

        // Convert the array of Study objects to a List
        List<Study> studiesList = Arrays.asList(studiesArray);

        // Return the list of studies
        return studiesList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processStudyChanges() {
        // Read studies from the database
        List<Study> studiesFromDB = studyRepository.findAll();

        // Retrieve the list of studies from the external API
        List<Study> studiesList = getAllStudies();

        // Create a map to store the mapping between study IDs from the external API and the database
        Map<Long, Long> studiesMap = createStudyMap(studiesList, studiesFromDB);

        // Save new studies to the database and update the map with corresponding IDs
        saveNewStudies(studiesList, studiesFromDB, studiesMap);

        // Return the map of study IDs
        return studiesMap;
    }

    private Map<Long, Long> createStudyMap(List<Study> studiesList, List<Study> studiesFromDB) {
        Map<Long, Long> studiesMap = new HashMap<>();
        for (Study study : studiesList) {
            for (Study studyFromDB : studiesFromDB) {
                if (study.getPubmedId().equals(studyFromDB.getPubmedId())) {
                    studiesMap.put(study.getId(), studyFromDB.getId());
                    break;
                }
            }
        }
        return studiesMap;
    }

    private void saveNewStudies(List<Study> studiesList, List<Study> studiesFromDB, Map<Long, Long> studiesMap) {
        for (Study study : studiesList) {
            boolean found = false;
            for (Study studyFromDB : studiesFromDB) {
                if (study.getPubmedId().equals(studyFromDB.getPubmedId())) {
                    found = true;
                    studiesMap.put(study.getId(), studyFromDB.getId());
                    break;
                }
            }
            if (!found) {
                Map<Long, Long> longLongMap = userController.processUsersChanges();
                Long aLong = longLongMap.get(study.getCreatedBy().getId());
                User byId = userRepository.findById(aLong).get();
                study.setUpdatedBy(byId);
                study.setCreatedBy(byId);
                studyRepository.save(study);
                studiesMap.put(study.getId(), study.getId());
            }
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}






