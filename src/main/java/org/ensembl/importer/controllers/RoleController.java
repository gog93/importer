package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.Role;
import org.ensembl.importer.repositories.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        // Create a RestTemplate object to make HTTP requests
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL of the external API from which to retrieve disease data
        String apiUrl = "http://localhost:8082/api/roles";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<Role[]> response = restTemplate.getForEntity(apiUrl, Role[].class);

        // Extract the array of Disease objects from the response body
        Role[] rolesArray = response.getBody();

        // Convert the array of Disease objects to a List
        List<Role> rolesList = Arrays.asList(rolesArray);

        // Return the list of diseases
        return rolesList;
    }

    @GetMapping("/process-changes")
    public Map<Integer, Integer> processRoleChanges() {
        // Read diseases from the database
        List<Role> rolesFromDB = roleRepository.findAll();

        // Retrieve the list of diseases from the external API
        List<Role> rolesList = getAllRoles();

        // Create a map to store the mapping between disease IDs from the external API and the database
        Map<Integer, Integer> rolesMap = createRoleMap(rolesList, rolesFromDB);

        // Save new diseases to the database and update the map with corresponding IDs
        saveNewRoles(rolesList, rolesFromDB, rolesMap);

        // Return the map of disease IDs
        return rolesMap;
    }

    private Map<Integer, Integer> createRoleMap(List<Role> rolesList, List<Role> rolesFromDB) {
        Map<Integer, Integer> rolesMap = new HashMap<>();
        for (Role role : rolesList) {
            for (Role roleFromDB : rolesFromDB) {
                if (role.getRoleName().equals(roleFromDB.getRoleName())) {
                    rolesMap.put(role.getId(), roleFromDB.getId());
                    break;
                }
            }
        }
        return rolesMap;
    }

    private void saveNewRoles(List<Role> roleList, List<Role> rolesFromDB, Map<Integer, Integer> rolesMap) {
        for (Role role : roleList) {
            boolean found = false;
            for (Role roleFromDB : rolesFromDB) {
                if (role.getRoleName().equals(roleFromDB.getRoleName())) {
                    found = true;
                    rolesMap.put(role.getId(), roleFromDB.getId());
                    break;
                }
            }
            if (!found) {
                role.setUpdatedAt(LocalDate.now());
                roleRepository.save(role);
                rolesMap.put(role.getId(), role.getId());
            }
        }
    }
}
