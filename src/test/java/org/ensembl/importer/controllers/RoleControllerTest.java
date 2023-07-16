package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Role;
import org.ensembl.importer.entities.SequenceVariation;
import org.ensembl.importer.repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RoleControllerTest extends TestHelper{
    @Mock
    private  RoleRepository roleRepository;
    @Mock
    private  RestTemplate restTemplate;
    private RoleController roleController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        roleController = new RoleController(roleRepository, restTemplate);
    }
    @Test
    void getAllRoles() {
        Role[] sequenceVariations = {role()};
        ResponseEntity<Role[]> responseEntity = new ResponseEntity<>(sequenceVariations, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<Role> allSequenceVariationsList = roleController.getAllRoles();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<Role> expectedVariationsList = Arrays.asList(sequenceVariations);
        assertEquals(expectedVariationsList, allSequenceVariationsList);
    }

    @Test
    void processRoleChanges() {
    }
}