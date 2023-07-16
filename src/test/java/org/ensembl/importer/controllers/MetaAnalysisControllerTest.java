package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.MetaAnalysis;
import org.ensembl.importer.repositories.MetaAnalysisRepository;
import org.ensembl.importer.repositories.UserRepository;
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

class MetaAnalysisControllerTest extends TestHelper {
    @Mock
    private MetaAnalysisRepository metaAnalysisRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserController userController;
    @Mock
    private RestTemplate restTemplate;
    private MetaAnalysisController metaAnalysisController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        metaAnalysisController = new MetaAnalysisController(metaAnalysisRepository,
                userRepository, userController, restTemplate);
    }

    @Test
    void getAllMetaAnalyses() {
        MetaAnalysis[] metaAnalyses1 = {metaAnalysis()};
        ResponseEntity<MetaAnalysis[]> responseEntity = new ResponseEntity<>(metaAnalyses1, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<MetaAnalysis> metaAnalyses = metaAnalysisController.getAllMetaAnalyses();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<MetaAnalysis> metaList = Arrays.asList(metaAnalyses1);
        assertEquals(metaList, metaAnalyses);
    }

    @Test
    void metaAnalysesChanges() {
    }
}