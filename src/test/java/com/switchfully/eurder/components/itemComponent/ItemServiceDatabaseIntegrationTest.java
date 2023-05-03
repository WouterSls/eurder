package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.exception.NoItemsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceDatabaseIntegrationTest {


    private IItemRepository itemRepository;
    private ItemMapper itemMapper = new ItemMapper();
    private Item testItem = new Item("foo","bar",10,5);
    @Autowired
    private IItemService itemService;

    @Test
    void getItemStock_itemPresent_returnsListOfItem(){
        //given
        CreateItemDTO testCreateItem = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), testItem.getAmount());
        ItemDTO expectedItem = itemService.createNewItem(testCreateItem);
        //when
        List<ItemDTO> actualList = itemService.getItemsSortedByUrgency();
        //then
        Assertions.assertEquals(List.of(expectedItem),actualList);
    }

    @Test
    void getItemsStock_itemsPresent_returnsListOfItemsSortedByUrgency(){
        CreateItemDTO testCreateItem = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = itemService.getItemsSortedByUrgency();

        Assertions.assertEquals(List.of(expectedItem2,expectedItem3,expectedItem1),actualList);
    }

    @Test
    void getItemsStock_noItemsPresent_returnsNoItemsException(){
        Assertions.assertThrows(NoItemsException.class, () ->{
           itemService.getItemsSortedByUrgency();
        });
    }

    @Test
    void getItemsStockByUrgency_itemsPresent_returnsListOfItemsByUrgency(){
        CreateItemDTO testCreateItem = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO(testItem.getName(), testItem.getDescription(), testItem.getPrice(), 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = itemService.getItemsOnUrgency("low");

        Assertions.assertEquals(List.of(expectedItem1),actualList);
    }
}
