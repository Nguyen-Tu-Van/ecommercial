package com.example.demo.controllers;

import com.example.demo.UtilTests;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        UtilTests.injectObjects(userController, "userRepository", userRepository);
        UtilTests.injectObjects(userController, "cartRepository", cartRepository);
        UtilTests.injectObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
    }

    @Test
    public void verifyCreateUser() throws Exception{
        when(passwordEncoder.encode("passWord123")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("tuvan");
        createUserRequest.setPassword("passWord123");
        createUserRequest.setConfirmPassword("passWord123");
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("tuvan", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void verifyFindById() throws Exception {
        when(passwordEncoder.encode("passWord123")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("tuvan");
        createUserRequest.setPassword("passWord123");
        createUserRequest.setConfirmPassword("passWord123");
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("tuvan", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        ResponseEntity<User> response2 = userController.findById(user.getId());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    public void verifyFindByUsername() throws Exception {
        when(passwordEncoder.encode("passWord123")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("tuvan");
        createUserRequest.setPassword("passWord123");
        createUserRequest.setConfirmPassword("passWord123");
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("tuvan", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<User> response2 = userController.findByUserName(user.getUsername());
        User userFound = response2.getBody();
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }
}
