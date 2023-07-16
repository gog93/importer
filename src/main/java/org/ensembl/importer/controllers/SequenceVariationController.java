package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.ensembl.importer.entities.Gene;
import org.ensembl.importer.entities.Patient;
import org.ensembl.importer.entities.SequenceVariation;
import org.ensembl.importer.repositories.GeneRepository;
import org.ensembl.importer.repositories.PatientRepository;
import org.ensembl.importer.repositories.SequenceVariationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping("/api/sequence-variations")
public class SequenceVariationController {
    private final GeneController geneController;
    private final PatientController patientController;
    private final GeneRepository geneRepository;
    private final PatientRepository patientRepository;
    private final SequenceVariationRepository sequenceVariationRepository;
   private final RestTemplate restTemplate;


    @GetMapping
    public List<SequenceVariation> getAllSequenceVariations() {

        // Define the URL of the external API from which to retrieve sequence variation data
        String apiUrl = "http://localhost:8082/api/sequence-variations";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<SequenceVariation[]> response = restTemplate.getForEntity(apiUrl, SequenceVariation[].class);

        // Extract the array of SequenceVariation objects from the response body
        SequenceVariation[] sequenceVariationsArray = response.getBody();

        // Convert the array of SequenceVariation objects to a List
        List<SequenceVariation> sequenceVariationsList = Arrays.asList(sequenceVariationsArray);

        // Return the list of sequence variations
        return sequenceVariationsList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processSequenceVariationChanges() {
        // Read studies from the database
        List<SequenceVariation> sequenceVariationFromDB = sequenceVariationRepository.findAll();

        // Retrieve the list of studies from the external API
        List<SequenceVariation> sequenceVariationList = getAllSequenceVariations();

        // Create a map to store the mapping between study IDs from the external API and the database
        Map<Long, Long> sequenceVariationMap = createSequenceVariationMap(sequenceVariationList, sequenceVariationFromDB);

        // Save new studies to the database and update the map with corresponding IDs
        saveNewSequenceVariation(sequenceVariationList, sequenceVariationFromDB, sequenceVariationMap);

        // Return the map of study IDs
        return sequenceVariationMap;
    }

    private Map<Long, Long> createSequenceVariationMap(List<SequenceVariation> sequenceVariationList, List<SequenceVariation> sequenceVariationFromDB) {
        Map<Long, Long> sequenceVariationsMap = new HashMap<>();
        for (SequenceVariation sequenceVariation : sequenceVariationList) {
            for (SequenceVariation sequenceVariationDB : sequenceVariationFromDB) {
                if (sequenceVariationDB.getPatient().getIndividualIdentity().equals(sequenceVariation.getPatient().getIndividualIdentity())
                        && sequenceVariation.getPatient().getStudy().getPubmedId()
                        .equals(sequenceVariationDB.getPatient().getStudy().getPubmedId())
                        && sequenceVariation.getAlias().equals(sequenceVariationDB.getAlias())) {
                    sequenceVariationsMap.put(sequenceVariation.getId(), sequenceVariationDB.getId());
                    break;
                }
            }
        }
        return sequenceVariationsMap;
    }

    private void saveNewSequenceVariation(List<SequenceVariation> sequenceVariationList, List<SequenceVariation> sequenceVariationsFromDB, Map<Long, Long> sequenceVariationsMap) {
        for (SequenceVariation sequenceVariation : sequenceVariationList) {
            boolean found = false;
            for (SequenceVariation sequenceVariationDB : sequenceVariationsFromDB) {
                if (sequenceVariation.getAlias().equals(sequenceVariationDB.getAlias())) {
                    found = true;
                    sequenceVariationsMap.put(sequenceVariation.getId(), sequenceVariationDB.getId());
                    break;
                }
            }
            if (!found) {
                Map<Long, Long> patientMap = patientController.processPatientChanges();
                Map<Integer, Integer> geneMap = geneController.processGeneChanges();
                Long patientById = patientMap.get(sequenceVariation.getPatient().getId());
                Integer geneById = geneMap.get(sequenceVariation.getGene().getId());
                Gene byIdGen = geneRepository.findById(geneById).get();
                Patient byIdPatient = patientRepository.findById(patientById).get();
                sequenceVariation.setGene(byIdGen);
                sequenceVariation.setPatient(byIdPatient);
                sequenceVariation.setUpdatedAt(LocalDateTime.now());
                sequenceVariationRepository.save(sequenceVariation);
                sequenceVariationsMap.put(sequenceVariation.getId(), sequenceVariation.getId());
            }
        }
    }
}
