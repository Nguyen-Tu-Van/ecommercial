package com.example.demo.controllers;

import com.example.demo.UtilTests;
import com.example.demo.model.persistence.Cart;
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
import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
    private CartController cartController;
    private UserController userController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        userController = new UserController();
        UtilTests.injectObjects(cartController, "userRepository", userRepository);
        UtilTests.injectObjects(cartController, "cartRepository", cartRepository);
        UtilTests.injectObjects(cartController, "itemRepository", itemRepository);
        UtilTests.injectObjects(userController, "userRepository", userRepository);
        UtilTests.injectObjects(userController, "cartRepository", cartRepository);
        UtilTests.injectObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
        when(userRepository.findByUsername("tuvan")).thenReturn(createUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(setItem()));
        when(cartRepository.save(any())).thenReturn(addCart());
    }

    public Cart addCart() {
        Cart cart = new Cart();
        cart.setId(Long.valueOf(1));
        cart.addItem(setItem());
        return cart;
    }

    private Item setItem(){
        Item item = new Item();
        item.setId(Long.valueOf(1));
        item.setDescription("Test set Item");
        item.setPrice(BigDecimal.valueOf(7,05));
        return item;
    }

    private User createUser() {
        User user = new User();
        user.setId(Long.valueOf(1));
        user.setUsername("tuvan");
        user.setPassword("testPassword");
        user.setCart(addCart());
        return user;
    }

    @Test
    public void verifyAddToCart() throws Exception{
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
        ResponseEntity<Cart> cart = cartController.addTocart(cartRequest);
        assertNotNull(cart);
        assertEquals(HttpStatus.OK, cart.getStatusCode());
    }

    @Test
    public void verifyRemoveFromCart() throws Exception {
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
        ResponseEntity<Cart> cart = cartController.removeFromcart(cartRequest);
        assertNotNull(cart);
        assertEquals(HttpStatus.OK, cart.getStatusCode());
    }

}
