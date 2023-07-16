package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.MetaStudyRelation;
import org.ensembl.importer.entities.Patient;
import org.ensembl.importer.repositories.MetaAnalysisRepository;
import org.ensembl.importer.repositories.MetaStudyRelationRepository;
import org.ensembl.importer.repositories.StudyRepository;
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

class MetaStudyRelationControllerTest extends TestHelper {
    @Mock
    private MetaStudyRelationRepository metaStudyRelationRepository;
    @Mock
    private StudyRepository studyRepository;
    @Mock
    private MetaAnalysisRepository metaAnalysisRepository;
    @Mock
    private StudyController studyController;
    @Mock
    private MetaAnalysisController metaAnalysisController;
    @Mock
    private RestTemplate restTemplate;
    private MetaStudyRelationController metaStudyRelationController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        metaStudyRelationController = new MetaStudyRelationController(metaStudyRelationRepository, studyRepository,
                metaAnalysisRepository,studyController,metaAnalysisController,restTemplate);
    }

    @Test
    void getAllMetaStudyRelations() {
        MetaStudyRelation[] metaStudyRelationsArray = {metaStudyRelation()};
        ResponseEntity<MetaStudyRelation[]> responseEntity = new ResponseEntity<>(metaStudyRelationsArray, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<MetaStudyRelation> metaStudyRelations = metaStudyRelationController.getAllMetaStudyRelations();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<MetaStudyRelation> metaStudyRelationList = Arrays.asList(metaStudyRelationsArray);
        assertEquals(metaStudyRelationList, metaStudyRelations);
    }

    @Test
    void metaStudyRelationsChanges() {
    }
}