package org.ensembl.importer.controllers;

import lombok.RequiredArgsConstructor;
import org.ensembl.importer.entities.Role;
import org.ensembl.importer.entities.User;
import org.ensembl.importer.repositories.RoleRepository;
import org.ensembl.importer.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleController roleController;


    @GetMapping
    public List<User> getAllUsers() {
        // Create a RestTemplate object to make HTTP requests

        // Define the URL of the external API from which to retrieve disease data
        String apiUrl = "http://localhost:8082/api/users";

        // Make a GET request to the external API and retrieve the response as ResponseEntity
        ResponseEntity<User[]> response = restTemplate.getForEntity(apiUrl, User[].class);

        // Extract the array of Disease objects from the response body
        User[] usersArray = response.getBody();

        // Convert the array of Disease objects to a List
        List<User> usersList = Arrays.asList(usersArray);

        // Return the list of diseases
        return usersList;
    }

    @GetMapping("/process-changes")
    public Map<Long, Long> processUsersChanges() {
        // Read diseases from the database
        List<User> usersFromDB = userRepository.findAll();

        // Retrieve the list of diseases from the external API
        List<User> usersList = getAllUsers();

        // Create a map to store the mapping between disease IDs from the external API and the database
        Map<Long, Long> usersMap = createUsersMap(usersList, usersFromDB);

        // Save new diseases to the database and update the map with corresponding IDs
        saveNewUsers(usersList, usersFromDB, usersMap);

        // Return the map of disease IDs
        return usersMap;
    }

    private Map<Long, Long> createUsersMap(List<User> usersList, List<User> usersFromDB) {
        Map<Long, Long> usersMap = new HashMap<>();
        for (User user : usersList) {
            for (User userFromDB : usersFromDB) {
                if (user.getFirstname().equals(userFromDB.getFirstname())) {
                    usersMap.put(user.getId(), user.getId());
                    break;
                }
            }
        }
        return usersMap;
    }

    private void saveNewUsers(List<User> usersList, List<User> usersFromDB, Map<Long, Long> usersMap) {
        for (User user : usersList) {
            boolean found = false;
            for (User userFromDB : usersFromDB) {
                if (user.getFirstname().equals(userFromDB.getFirstname())) {
                    found = true;
                    usersMap.put(user.getId(), userFromDB.getId());
                    break;
                }
            }
            if (!found) {

                Map<Integer, Integer> integerIntegerMap = roleController.processRoleChanges();
                Integer aLong = integerIntegerMap.get(user.getRole().getId());
                Role byId = roleRepository.findById(aLong).get();
                user.setRole(byId);
                user.setUpdatedAt(Timestamp.from(Instant.now()));
                userRepository.save(user);
                usersMap.put(user.getId(), user.getId());
            }
        }
    }
}
