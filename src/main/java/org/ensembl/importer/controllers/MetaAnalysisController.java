package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.ensembl.importer.entities.MetaAnalysis;
import org.ensembl.importer.entities.User;
import org.ensembl.importer.repositories.MetaAnalysisRepository;
import org.ensembl.importer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meta-analyses")
public class MetaAnalysisController {

    private final MetaAnalysisRepository metaAnalysisRepository;
    private final UserRepository userRepository;
    private final UserController userController;
   private final RestTemplate restTemplate;


    @GetMapping
    public List<MetaAnalysis> getAllMetaAnalyses() {

        // Define the URL of the external API from which to retrieve meta-analysis data
        String apiUrl = "http://localhost:8082/api/meta-analyses";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<MetaAnalysis[]> response = restTemplate.getForEntity(apiUrl, MetaAnalysis[].class);

        // Extract the array of MetaAnalysis objects from the response body
        MetaAnalysis[] metaAnalysesArray = response.getBody();

        // Convert the array of MetaAnalysis objects to a List
        List<MetaAnalysis> metaAnalysesList = Arrays.asList(metaAnalysesArray);

        // Return the list of meta-analyses
        return metaAnalysesList;
    }
    @GetMapping("/process-changes")
    public Map<Long, Long> metaAnalysesChanges() {
        // Read studies from the database
        List<MetaAnalysis> metaAnalysesFromDB = metaAnalysisRepository.findAll();

        // Retrieve the list of studies from the external API
        List<MetaAnalysis> metaAnalysesList = getAllMetaAnalyses();

        // Create a map to store the mapping between study IDs from the external API and the database
        Map<Long, Long> metaAnalysesMap = createpatientsSymptomMap(metaAnalysesList, metaAnalysesFromDB);

        // Save new studies to the database and update the map with corresponding IDs
        saveNewPatientsSymptoms(metaAnalysesList, metaAnalysesFromDB, metaAnalysesMap);

        // Return the map of study IDs
        return metaAnalysesMap;
    }

    private Map<Long, Long> createpatientsSymptomMap(List<MetaAnalysis> metaAnalysesList, List<MetaAnalysis> metaAnalysesFromDB) {
        Map<Long, Long> metaAnalysesMap = new HashMap<>();
        for (MetaAnalysis metaAnalysis : metaAnalysesList) {
            for (MetaAnalysis sequenceVariationDB : metaAnalysesFromDB) {
                if (metaAnalysis.getMetaName().equals(metaAnalysis.getMetaName())) {
                    metaAnalysesMap.put(metaAnalysis.getId(), sequenceVariationDB.getId());
                    break;
                }
            }
        }
        return metaAnalysesMap;
    }

    private void saveNewPatientsSymptoms(List<MetaAnalysis> metaAnalysesList, List<MetaAnalysis> metaAnalysesFromDB, Map<Long, Long> metaAnalysesMap) {
        for (MetaAnalysis metaAnalyses : metaAnalysesList) {
            boolean found = false;
            for (MetaAnalysis metaAnalysesDB : metaAnalysesFromDB) {
                if (metaAnalyses.getMetaName().equals(metaAnalysesDB.getMetaName())) {
                    found = true;
                    metaAnalysesMap.put(metaAnalyses.getId(), metaAnalysesDB.getId());
                    break;
                }
            }
            if (!found) {
                Map<Long, Long> longLongMap = userController.processUsersChanges();
                Long aLong = longLongMap.get(metaAnalyses.getCreatedBy().getId());
                User byId = userRepository.findById(aLong).get();
                metaAnalyses.setUpdatedBy(byId);
                metaAnalyses.setCreatedBy(byId);
                metaAnalyses.setUpdatedAt(LocalDateTime.now());
                metaAnalysisRepository.save(metaAnalyses);
                metaAnalysesMap.put(metaAnalyses.getId(), metaAnalyses.getId());
            }
        }
    }
}
