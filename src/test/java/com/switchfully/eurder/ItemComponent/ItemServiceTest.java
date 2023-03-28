package com.switchfully.eurder.ItemComponent;

import com.switchfully.eurder.zExceptions.IllegalAmountException;
import com.switchfully.eurder.zExceptions.IllegalPriceException;
import com.switchfully.eurder.zExceptions.MandatoryFieldException;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;


class ItemServiceTest {

    @Nested
    @DisplayName("unit tests with mocking")
    class unitTestMocking{

        private ItemService itemService;
        private ItemRepository itemRepositoryMock;
        private ItemMapper itemMapperMock;

        CreateItemDTO TEST_ITEM_CREATE = new CreateItemDTO("foo","bar",10,2);

        @BeforeEach
        void setup(){
            itemRepositoryMock = Mockito.mock(ItemRepository.class);
            itemMapperMock = Mockito.mock(ItemMapper.class);
            itemService = new ItemService(itemRepositoryMock,itemMapperMock);
        }

        @Test
        void createNewItem_CreateItemDTOPresent_ReturnsItemDTO(){
            itemService.createNewItem(TEST_ITEM_CREATE);

            Mockito.verify(itemRepositoryMock).addItem(itemMapperMock.mapToDomain(TEST_ITEM_CREATE));
        }

        @Test
        void createNewItem_CreateItemDTONotPresent_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                itemService.createNewItem(null);
            });
        }

       @Test
       void createNewItem_CreateItemDTONameNotPresent_returnsMandatoryFieldException(){
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
        void createNewItem_CreateItemDTODescriptionNotPresent_returnsMandatoryFieldException(){
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
        void createNewItem_CreateItemDTOPriceEquals0_returnsIllegalPriceException(){
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
        void createNewItem_CreateItemDTOPriceLowerThan0_returnsIllegalPriceException(){
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
        void createNewItem_CreateItemDTOAmountEquals0_returnsIllegalAmountException(){
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
        void createNewItem_CreateItemDTOAmountLowerThan0_returnsIllegalAmountException(){
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                itemService.createNewItem(
                        new CreateItemDTO("foo",
                                "bar",
                                10,
                                -1)
                );
            });
        }
    }

}