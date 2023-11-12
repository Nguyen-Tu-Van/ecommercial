package com.example.demo.controllers;

import com.example.demo.UtilTests;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() throws Exception {
        orderController = new OrderController();
        UtilTests.injectObjects(orderController, "orderRepository", orderRepository);
        UtilTests.injectObjects(orderController, "userRepository", userRepository);
        when(userRepository.findByUsername("tuvan")).thenReturn(createUser());
        when(orderRepository.save(any())).thenReturn(createOrder());
        when(orderRepository.findByUser(any())).thenReturn(List.of(createOrder()));
    }

    public Cart addCart() {
        Cart cart = new Cart();
        cart.setId(Long.valueOf(1));
        cart.addItem(setItem());
        return cart;
    }

    private UserOrder createOrder() {
        return UserOrder.createFromCart(addCart());
    }

    private Item setItem(){
        Item item = new Item();
        item.setId(Long.valueOf(1));
        item.setPrice(BigDecimal.valueOf(7,05));
        item.setDescription("Test set Item");
        return item;
    }

    private User createUser() {
        User user = new User();
        user.setId(Long.valueOf(1));
        user.setUsername("tuvan");
        user.setPassword("passWord123");
        user.setCart(addCart());
        return user;
    }

    @Test
    public void verifySubmitOrderOfUser() {
        ResponseEntity<UserOrder> userOrder = orderController.submit("tuvan");
        assertEquals(HttpStatus.OK, userOrder.getStatusCode());
    }

    @Test
    public void verifyGetOrdersForUser() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("tuvan");
        assertEquals(HttpStatus.OK, ordersForUser.getStatusCode());
    }
}
