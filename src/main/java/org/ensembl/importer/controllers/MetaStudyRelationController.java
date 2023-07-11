package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.MetaAnalysis;
import org.ensembl.importer.entities.MetaStudyRelation;
import org.ensembl.importer.entities.Study;
import org.ensembl.importer.repositories.MetaAnalysisRepository;
import org.ensembl.importer.repositories.MetaStudyRelationRepository;
import org.ensembl.importer.repositories.StudyRepository;
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
@RequestMapping("/api/meta-study-relations")
public class MetaStudyRelationController {

    private final MetaStudyRelationRepository metaStudyRelationRepository;
    private final StudyRepository studyRepository;
    private final MetaAnalysisRepository metaAnalysisRepository;
    private final StudyController studyController;
    private final MetaAnalysisController metaAnalysisController;

    @Autowired
    public MetaStudyRelationController(MetaStudyRelationRepository metaStudyRelationRepository, StudyRepository studyRepository, MetaAnalysisRepository metaAnalysisRepository, StudyController studyController, MetaAnalysisController metaAnalysisController) {
        this.metaStudyRelationRepository = metaStudyRelationRepository;
        this.studyRepository = studyRepository;
        this.metaAnalysisRepository = metaAnalysisRepository;
        this.studyController = studyController;
        this.metaAnalysisController = metaAnalysisController;
    }

    @GetMapping
    public List<MetaStudyRelation> getAllMetaStudyRelations() {
        // Create a RestTemplate object to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL of the external API from which to retrieve meta-study relation data
        String apiUrl = "http://localhost:8082/api/meta-study-relations";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<MetaStudyRelation[]> response = restTemplate.getForEntity(apiUrl, MetaStudyRelation[].class);

        // Extract the array of MetaStudyRelation objects from the response body
        MetaStudyRelation[] metaStudyRelationsArray = response.getBody();

        // Convert the array of MetaStudyRelation objects to a List
        List<MetaStudyRelation> metaStudyRelationsList = Arrays.asList(metaStudyRelationsArray);

        // Return the list of meta-study relations
        return metaStudyRelationsList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> metaStudyRelationsChanges() {
        // Read studies from the database
        List<MetaStudyRelation> metaStudyRelationFromDB = metaStudyRelationRepository.findAll();

        // Retrieve the list of studies from the external API
        List<MetaStudyRelation> metaStudyRelationList = getAllMetaStudyRelations();

        // Create a map to store the mapping between study IDs from the external API and the database
        Map<Long, Long> metaStudyRelationMap = createMetaStudyRelationMap(metaStudyRelationList, metaStudyRelationFromDB);

        // Save new studies to the database and update the map with corresponding IDs
        saveMetaStudyRelations(metaStudyRelationList, metaStudyRelationFromDB, metaStudyRelationMap);

        // Return the map of study IDs
        return metaStudyRelationMap;
    }

    private Map<Long, Long> createMetaStudyRelationMap(List<MetaStudyRelation> metaStudyRelationList, List<MetaStudyRelation> metaStudyRelationFromDB) {
        Map<Long, Long> metaStudyRelationMap = new HashMap<>();
        for (MetaStudyRelation metaStudyRelation : metaStudyRelationList) {
            for (MetaStudyRelation metaStudyRelationDB : metaStudyRelationFromDB) {
                if (metaStudyRelationDB.getStudy().getPubmedId().equals(metaStudyRelation.getStudy().getPubmedId())) {
                    metaStudyRelationMap.put(metaStudyRelation.getId(), metaStudyRelationDB.getId());
                    break;
                }
            }
        }
        return metaStudyRelationMap;
    }

    private void saveMetaStudyRelations(List<MetaStudyRelation> metaStudyRelationList, List<MetaStudyRelation> metaStudyRelationFromDB, Map<Long, Long> metaStudyRelationMap) {
        for (MetaStudyRelation metaStudyRelation : metaStudyRelationList) {
            boolean found = false;
            for (MetaStudyRelation metaStudyRelationDB : metaStudyRelationFromDB) {
                if (metaStudyRelation.getStudy().getPubmedId().equals(metaStudyRelation.getStudy().getPubmedId())) {
                    found = true;
                    metaStudyRelationMap.put(metaStudyRelation.getId(), metaStudyRelationDB.getId());
                    break;
                }
            }
            if (!found) {
                Map<Long, Long> studyMap = studyController.processStudyChanges();
                Map<Long, Long> metaAnalysesMap = metaAnalysisController.metaAnalysesChanges();
                Long aStudy = studyMap.get(metaStudyRelation.getStudy().getId());
                Long aMetaAnalyses = metaAnalysesMap.get(metaStudyRelation.getMetaAnalysis().getId());
                Study byIdStudy = studyRepository.findById(aStudy).get();
                MetaAnalysis byIdMetaAnalysis = metaAnalysisRepository.findById(aMetaAnalyses).get();
                metaStudyRelation.setStudy(byIdStudy);
                metaStudyRelation.setMetaAnalysis(byIdMetaAnalysis);
                metaStudyRelationRepository.save(metaStudyRelation);
                metaStudyRelationMap.put(metaStudyRelation.getId(), metaStudyRelation.getId());
            }
        }
    }
}
