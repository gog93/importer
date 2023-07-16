package org.ensembl.importer.controllers;

import org.ensembl.importer.entities.User;
import org.ensembl.importer.repositories.RoleRepository;
import org.ensembl.importer.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)

class UserControllerTest extends TestHelper {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleController roleController;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController( restTemplate,userRepository,roleRepository,roleController);
    }

    @Test
    void getAllUsers() {

        User[] usersArray = {user()};
        ResponseEntity<User[]> responseEntity = new ResponseEntity<>(usersArray, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any(Class.class))).thenReturn(responseEntity);

        List<User> usersList = userController.getAllUsers();

        verify(restTemplate, times(1)).getForEntity(anyString(), any());

        List<User> expectedUsersList = Arrays.asList(usersArray);
        assertEquals(expectedUsersList, usersList);
    }

    @Test
    void processUsersChanges() {
    }
}