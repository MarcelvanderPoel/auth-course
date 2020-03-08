package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    // Mocks

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {

        cartController = new CartController();

        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);

    }

    @Test
    public void add_to_cart_happy_path() throws Exception {

        // Stub



        User user = new User();
        user.setUsername("testUserName");
        user.setId(3L);
        user.setPassword("testUserPassword");
        Cart cart = new Cart();
        cart.setId(2L);
        cart.setUser(user);
        user.setCart(cart);
        when(userRepository.findByUsername("testUserName")).thenReturn(user);

        Item item = new Item();
        item.setId(3L);
        item.setDescription("itemDescription");
        item.setPrice(BigDecimal.valueOf(25));
        item.setName("itemName");
        when(itemRepository.findById(3L)).thenReturn(java.util.Optional.of(item));

        // Given

        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("testUserName");
        r.setItemId(3L);
        r.setQuantity(4);

        // When

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        // Then

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(100),c.getTotal());
    }


    @Test
    public void add_to_cart_not_authorized() throws Exception {

        // Stub

        // when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        // Given

        ModifyCartRequest r = new ModifyCartRequest();
        r.setUsername("testUserName");
        r.setItemId(3L);
        r.setQuantity(4);

        // When

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        // Then

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }



}
