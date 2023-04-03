package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.exception.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.UUID;


class ItemServiceTest {

    @Nested
    @DisplayName("ItemService unit test")
    class unitTest {

        private ItemService itemService;
        private ItemRepository itemRepositoryMock;
        private ItemMapper itemMapperMock;

        private final CreateItemDTO TEST_ITEM_CREATE_DTO = new CreateItemDTO("foo", "bar", 10, 2);
        private final UpdateItemDTO TEST_ITEM_UPDATE_DTO = new UpdateItemDTO("bar","foo",20,6);

        @BeforeEach
        void setup() {
            itemRepositoryMock = Mockito.mock(ItemRepository.class);
            itemMapperMock = Mockito.mock(ItemMapper.class);
            itemService = new ItemService(itemRepositoryMock, itemMapperMock);
        }

        @Test
        void createNewItem_CreateItemDTOPresent_returnsItemDTO() {
            itemService.createNewItem(TEST_ITEM_CREATE_DTO);

            Mockito.verify(itemRepositoryMock).addItem(itemMapperMock.mapToDomain(TEST_ITEM_CREATE_DTO));
        }

        @Test
        void createNewItem_CreateItemDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.createNewItem(null);
            });
        }

        @Test
        void createNewItem_CreateItemDTONameNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO(null,
                                "test",
                                10,
                                1)
                );
            });
        }

        @Test
        void createNewItem_CreateItemDTODescriptionNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO("foo",
                                null,
                                10,
                                1)
                );
            });
        }

        @Test
        void createNewItem_CreateItemDTOPriceEquals0_returnsIllegalPriceException() {
            Assertions.assertThrows(IllegalPriceException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO("foo",
                                "bar",
                                0,
                                1)
                );
            });
        }

        @Test
        void createNewItem_CreateItemDTOPriceLowerThan0_returnsIllegalPriceException() {
            Assertions.assertThrows(IllegalPriceException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO("foo",
                                "bar",
                                -5,
                                1)
                );
            });
        }

        @Test
        void createNewItem_CreateItemDTOAmountEquals0_returnsIllegalAmountException() {
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO("foo",
                                "bar",
                                10,
                                0)
                );
            });
        }

        @Test
        void createNewItem_CreateItemDTOAmountLowerThan0_returnsIllegalAmountException() {
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO("foo",
                                "bar",
                                10,
                                -1)
                );
            });
        }

        @Test
        void getItemByName_NamePresent_ReturnsItemDTO() {

        }

        @Test
        void getItemByName_IncorrectName_returnsNull() {

        }

        @Test
        void getItemByName_NameNotPresent_returnsNull() {

        }

        @Test
        void getListItemDTO_ListNotPresent_returnsEmptyList() {

        }

        @Test
        void getListItemDTO_ListPresent_returnListOfItemDTO() {

        }

        @Test
        void updateItem_updateItemDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.updateItemById(null, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentCorrectId_updatesItemRepo(){
    //TODO: test in integration test

//            Item testItem = new Item("foo","bar",10,2);
//            ItemRepository itemRepo = new ItemRepository();
//            itemRepo.addItem(testItem);
//            UUID itemId = testItem.getId();
//            ItemMapper itemMapper = Mockito.mock(ItemMapper.class);
//            ItemService iswd = new ItemService(itemRepo,itemMapper);
//
//
//            UpdateItemDTO updateItemTest = new UpdateItemDTO("bar","foo",20,10);
//
//            iswd.updateItemById(updateItemTest,itemId.toString());
//
//            Mockito.verify(itemRepo).updateItem(testItem);
//            Mockito.verify(itemRepositoryMock, Mockito.never()).addItem(testItem);
//            Mockito.verify(itemRepositoryMock, Mockito.never()).getItems();
//            Mockito.verify(itemRepositoryMock, Mockito.never()).getItemById(testItem.getId());
        }
        @Test
        void updateItem_UpdateItemDTOPresentNameNotPresent_returnsMandatoryFieldException(){
            UpdateItemDTO testUpdateItem =new UpdateItemDTO(null,"bar",10,4);

            Assertions.assertThrows(MandatoryFieldException.class, () -> {
               itemService.updateItemById(testUpdateItem,UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentDescriptionNotPresent_returnsMandatoryFieldException(){
            UpdateItemDTO testUpdateItem =new UpdateItemDTO("foo",null,10,4);

            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.updateItemById(testUpdateItem,UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentPriceEquals0_returnsMandatoryFieldException(){
            UpdateItemDTO testUpdateItem =new UpdateItemDTO("foo","bar",0,4);

            Assertions.assertThrows(IllegalPriceException.class, () -> {
                itemService.updateItemById(testUpdateItem,UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentPriceUnder0_returnsMandatoryFieldException(){
            UpdateItemDTO testUpdateItem =new UpdateItemDTO("foo","bar",-10,4);

            Assertions.assertThrows(IllegalPriceException.class, () -> {
                itemService.updateItemById(testUpdateItem,UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentAmountUnder0_returnsMandatoryFieldException(){
            UpdateItemDTO testUpdateItem =new UpdateItemDTO("foo","bar",10,-5);

            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.updateItemById(testUpdateItem,UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentAmountEquals0_returnsMandatoryFieldException(){
            UpdateItemDTO testUpdateItem =new UpdateItemDTO("foo","bar",10,0);

            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.updateItemById(testUpdateItem,UUID.randomUUID());
            });
        }

        @Test
        void getItemsStock_ItemsNotPresent_returnsNoItemsException(){
            Assertions.assertThrows(NoItemsException.class, () -> {
               itemService.getItemsSortedByUrgency();
            });
        }

    }
}