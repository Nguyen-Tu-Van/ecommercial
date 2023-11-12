package com.example.demo.controllers;

import com.example.demo.UtilTests;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
    private UserController userController;
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        userController = new UserController();
        UtilTests.injectObjects(itemController, "itemRepository", itemRepository);
        UtilTests.injectObjects(userController, "userRepository", userRepository);
        UtilTests.injectObjects(userController, "cartRepository", cartRepository);
        UtilTests.injectObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
        when(itemRepository.findByName(any())).thenReturn(null);
        when(itemRepository.findAll()).thenReturn(null);
    }

    @Test
    public void verifyGetItems() throws Exception{
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
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);

        ResponseEntity<List<Item>> items = itemController.getItems();
        assertEquals(HttpStatus.OK, items.getStatusCode());
    }

    @Test
    public void verifyGetItemsByName() throws Exception {
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
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);

        ResponseEntity<List<Item>> items = itemController.getItemsByName(user.getUsername());
        assertEquals(HttpStatus.NOT_FOUND, items.getStatusCode());
    }
}

