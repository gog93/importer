package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.*;
import org.ensembl.importer.repositories.GeneRepository;
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

@RestController
@RequestMapping("/api/genes")
public class GeneController {

    private final GeneRepository geneRepository;

    public GeneController(GeneRepository geneRepository) {
        this.geneRepository = geneRepository;
    }

    @GetMapping
    public List<Gene> getAllGenes() {
        // Create a RestTemplate object to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL of the external API from which to retrieve gene data
        String apiUrl = "http://localhost:8082/api/genes";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<Gene[]> response = restTemplate.getForEntity(apiUrl, Gene[].class);

        // Extract the array of Gene objects from the response body
        Gene[] genesArray = response.getBody();

        // Convert the array of Gene objects to a List
        List<Gene> genesList = Arrays.asList(genesArray);

        // Return the list of genes
        return genesList;
    }

    @GetMapping("/process-changes")
    public Map<Integer, Integer> processGeneChanges() {
        // Read genes from the database
        List<Gene> genesFromDB = geneRepository.findAll();

        // Retrieve the list of genes from the external API
        List<Gene> genesList = getAllGenes();

        // Create a map to store the mapping between gene IDs from the external API and the database
        Map<Integer, Integer> genesMap = createGeneMap(genesList, genesFromDB);

        // Save new genes to the database and update the map with corresponding IDs
        saveNewGenes(genesList, genesFromDB, genesMap);

        // Return the map of gene IDs
        return genesMap;
    }

    private Map<Integer, Integer> createGeneMap(List<Gene> genesList, List<Gene> genesFromDB) {
        Map<Integer, Integer> genesMap = new HashMap<>();
        for (Gene gene : genesList) {
            for (Gene geneFromDB : genesFromDB) {
                if (gene.getName().equals(geneFromDB.getName())) {
                    genesMap.put(gene.getId(), geneFromDB.getId());
                    break;
                }
            }
        }
        return genesMap;
    }

    private void saveNewGenes(List<Gene> genesList, List<Gene> genesFromDB, Map<Integer, Integer> genesMap) {
        for (Gene gene : genesList) {
            boolean found = false;
            for (Gene geneFromDB : genesFromDB) {
                if (gene.getName().equals(geneFromDB.getName())) {
                    found = true;
                    genesMap.put(gene.getId(), geneFromDB.getId());
                    break;
                }
            }
            if (!found) {
                gene.setUpdatedAt(LocalDateTime.now());
                geneRepository.save(gene);
                genesMap.put(gene.getId(), gene.getId());
            }
        }
    }
}