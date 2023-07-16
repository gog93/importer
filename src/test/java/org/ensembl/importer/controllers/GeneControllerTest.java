package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Gene;
import org.ensembl.importer.entities.MetaAnalysis;
import org.ensembl.importer.repositories.GeneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GeneControllerTest extends TestHelper {
    @Mock
    private GeneRepository geneRepository;
    @Mock
    private RestTemplate restTemplate;
    private GeneController geneController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        geneController = new GeneController(geneRepository, restTemplate);
    }

    @Test
    void getAllGenes() {
        Gene[] genes = {gene()};
        ResponseEntity<Gene[]> responseEntity = new ResponseEntity<>(genes, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<Gene> geneList = geneController.getAllGenes();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<Gene> geneList1 = Arrays.asList(genes);
        assertEquals(geneList1, geneList);
    }

    @Test
    void processGeneChanges() {
    }
}