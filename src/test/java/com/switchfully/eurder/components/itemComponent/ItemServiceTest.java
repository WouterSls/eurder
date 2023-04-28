package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.ItemMapper;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.IllegalPriceException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.exception.NoItemsException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


class ItemServiceTest {

    @Nested
    @DisplayName("ItemService unit test")
    class unitTest {

        private ItemService itemService;
        private IItemRepository itemRepositoryMock;
        private ItemMapper itemMapperMock;
        private IItemRepository itemRepository;

        private final CreateItemDTO TEST_ITEM_CREATE_DTO = new CreateItemDTO("foo", "bar", 10, 2);
        private final UpdateItemDTO TEST_ITEM_UPDATE_DTO = new UpdateItemDTO("bar", "foo", 20, 6);

        @BeforeEach
        void setup() {
            itemRepositoryMock = Mockito.mock(IItemRepository.class);
            itemMapperMock = Mockito.mock(ItemMapper.class);
            itemService = new ItemService(itemMapperMock,itemRepository);
        }

        @Test
        void createNewItem_CreateItemDTOPresent_returnsItemDTO() {
            itemService.createNewItem(TEST_ITEM_CREATE_DTO);

            Mockito.verify(itemRepositoryMock).save(itemMapperMock.mapToDomain(TEST_ITEM_CREATE_DTO));
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
        void getItemsSortedOnUrgency_ListNotPresent_returnsNoItemsException() {
            Assertions.assertThrows(NoItemsException.class, () ->{
               itemService.getItemsSortedByUrgency();
            });
        }

        @Test
        void getItemsSortedOnUrgency_ListPresentNoItemsPresent_returnListWithNullItem() {
            Item testItem = new Item("foo", "bar", 10, 5);

            Mockito.when(itemRepositoryMock.findAll())
                    .thenReturn(List.of(testItem));

            List<ItemDTO> actualList = itemService.getItemsSortedByUrgency();

            Assertions.assertFalse(actualList.isEmpty());
        }

        @Test
        void updateItem_updateItemDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.updateItemById(null, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentCorrectId_updatesItemRepo() {
            Item testItem = new Item("foo", "bar", 10, 5);

            Mockito.when(itemRepositoryMock.findAll())
                    .thenReturn(List.of(testItem));

            Mockito.when(itemRepositoryMock.findById(testItem.getId()))
                    .thenReturn(Optional.of(testItem));

            ItemDTO updateItem = itemService.updateItemById(new UpdateItemDTO("fizz", "buzz", 5, 20), testItem.getId());

            Assertions.assertEquals(itemMapperMock.mapToDTO(testItem), updateItem);
        }

        @Test
        void updateItem_UpdateItemDTOPresentNameNotPresent_returnsMandatoryFieldException() {
            UpdateItemDTO testUpdateItem = new UpdateItemDTO(null, "bar", 10, 4);

            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.updateItemById(testUpdateItem, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentDescriptionNotPresent_returnsMandatoryFieldException() {
            UpdateItemDTO testUpdateItem = new UpdateItemDTO("foo", null, 10, 4);

            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.updateItemById(testUpdateItem, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentPriceEquals0_returnsMandatoryFieldException() {
            UpdateItemDTO testUpdateItem = new UpdateItemDTO("foo", "bar", 0, 4);

            Assertions.assertThrows(IllegalPriceException.class, () -> {
                itemService.updateItemById(testUpdateItem, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentPriceUnder0_returnsMandatoryFieldException() {
            UpdateItemDTO testUpdateItem = new UpdateItemDTO("foo", "bar", -10, 4);

            Assertions.assertThrows(IllegalPriceException.class, () -> {
                itemService.updateItemById(testUpdateItem, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentAmountUnder0_returnsMandatoryFieldException() {
            UpdateItemDTO testUpdateItem = new UpdateItemDTO("foo", "bar", 10, -5);

            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.updateItemById(testUpdateItem, UUID.randomUUID());
            });
        }

        @Test
        void updateItem_UpdateItemDTOPresentAmountEquals0_returnsMandatoryFieldException() {
            UpdateItemDTO testUpdateItem = new UpdateItemDTO("foo", "bar", 10, 0);

            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.updateItemById(testUpdateItem, UUID.randomUUID());
            });
        }

        @Test
        void getItemsStock_ItemsNotPresent_returnsNoItemsException() {
            Assertions.assertThrows(NoItemsException.class, () -> {
                itemService.getItemsSortedByUrgency();
            });
        }

    }
}