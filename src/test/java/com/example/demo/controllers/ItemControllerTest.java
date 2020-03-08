package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;


    // Mocks

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);

    }


    @Test
    public void get_item_by_name_happy_flow() throws Exception {

        Item i = new Item();
        i.setId(0L);
        i.setDescription("First Item");
        i.setName("ItemUno");
        i.setPrice(BigDecimal.valueOf(300));
        List<Item> itemList = new ArrayList<>();
        itemList.add(i);

        when(itemRepository.findByName("itemUno")).thenReturn(itemList);

        final ResponseEntity<List<Item>> getResponse = itemController.getItemsByName("itemUno");

        List<Item> result = getResponse.getBody();
        assertNotNull(result);
        assertEquals(result.get(0).getDescription(), "First Item");
        assertEquals(200, getResponse.getStatusCodeValue());

    }

    @Test
    public void get_item_by_name_not_found() throws Exception {

        Item i = new Item();
        i.setId(0L);
        i.setDescription("First Item");
        i.setName("ItemUno");
        i.setPrice(BigDecimal.valueOf(300));
        List<Item> itemList = new ArrayList<>();
        itemList.add(i);

        when(itemRepository.findByName("itemUno")).thenReturn(itemList);

        final ResponseEntity<List<Item>> getResponse = itemController.getItemsByName("itemDue");
        assertNotNull(getResponse);
        assertEquals(404, getResponse.getStatusCodeValue());

    }

}
