package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    // Mocks

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private CartRepository cartRepository  = mock(CartRepository.class);

    @Before
    public void setup() {

        orderController = new OrderController();

        TestUtils.injectObjects(orderController,"userRepository", userRepository);
        TestUtils.injectObjects(orderController,"orderRepository", orderRepository);

        // Stub
        Item item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(20));
        item.setDescription("testItemDescription");

        User testUser = new User();
        testUser.setId(0L);
        testUser.setPassword("blabla");
        testUser.setUsername("testUser");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(testUser);
        cart.addItem(item);
        cartRepository.save(cart);
        testUser.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(testUser);


    }

    @Test
    public void create_order_happy_path() throws Exception {

        final ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(0L,userOrder.getUser().getId());
        assertEquals(BigDecimal.valueOf(20), userOrder.getUser().getCart().getTotal());
    }

    @Test
    public void create_order_unhappy_path() throws Exception {

        final ResponseEntity<UserOrder> response = orderController.submit("testWrongUser");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
