package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.CustomerMapper;
import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.InvalidIdFormatException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.exception.NoCustomersException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;



//Test logic in the customer service
class CustomerServiceTest {

    @Nested
    @DisplayName("CustomerService logic test")
    class unitTestMocking {

        private CustomerService customerService;
        private ICustomerRepository customerRepoMock;
        private CustomerMapper customerMapperMock;


        private final Customer TEST_CUSTOMER = new Customer(UUID.randomUUID(), "foo", "bar", "foobar@email.com", "fooStreetBar", "0032foo456bar");
        private final CreateCustomerDTO TEST_CUSTOMER_CREATE = new CreateCustomerDTO("begijnhof", "04123456");

        @BeforeEach
        void setup() {
            customerMapperMock = Mockito.mock(CustomerMapper.class);
            customerRepoMock = Mockito.mock(ICustomerRepository.class);
            customerService = new CustomerService(customerMapperMock, customerRepoMock);
        }

        @Test
        void getCustomerById_CustomersPresent_returnsCustomer() {
            String testUUID = UUID.randomUUID().toString();
            Mockito.when(customerRepoMock.findById(UUID.fromString(testUUID)))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            customerService.getCustomerById(testUUID);

            Mockito.verify(customerRepoMock).findById(UUID.fromString(testUUID));
            Mockito.verify(customerMapperMock).mapToDTO(TEST_CUSTOMER);
        }

        @Test
        void getCustomerId_CustomersNotPresent_returnsIllegalIdException() {
            Assertions.assertThrows(IllegalIdException.class, () -> {
                customerService.getCustomerById(TEST_CUSTOMER.getId().toString());
            });

        }

        @Test
        void getCustomerId_CustomerPresentIncorrectId_returnsIllegalIdException() {
            Mockito.when(customerRepoMock.findById(TEST_CUSTOMER.getId()))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            Assertions.assertThrows(IllegalIdException.class, () -> {
                customerService.getCustomerById(UUID.randomUUID().toString());
            });
        }

        @Test
        void getCustomerById_CustomerPresentIncorrectIdFormat_returnsIllegalArgumentException() {

            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                customerService.getCustomerById("foo");
            });
        }

        @Test
        void getListCustomerDTO_noCustomers_returnsEmptyList() {
            Mockito.when(customerRepoMock.findAll())
                    .thenReturn(emptyList());

            Assertions.assertThrows(NoCustomersException.class, () -> {
                customerService.getAllCustomers();
            });
        }

        @Test
        void getListCustomerDTO_CustomersPresent_returnsListOfCustomers() {
            Mockito.when(customerRepoMock.findAll())
                    .thenReturn(List.of(TEST_CUSTOMER));

            List<CustomerDTO> actualList = customerService.getAllCustomers();

            Assertions.assertEquals(customerMapperMock.mapToDTO(List.of(TEST_CUSTOMER)), actualList);
        }

        //
        @Test
        void createNewCustomer_CreateCustomerDTOPresent_savesCustomer() {

            customerService.createNewCustomer(null, TEST_CUSTOMER_CREATE);

            Mockito.verify(customerRepoMock).save(customerMapperMock.mapToDomain(null, TEST_CUSTOMER_CREATE));
        }

        @Test
        void createNewCustomer_CreateCustomerDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(null, null);
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOAddressNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(null, new CreateCustomerDTO(
                        null,
                        "testNumber")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPhoneNumberNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(null, new CreateCustomerDTO(
                        "testAddress",
                        null)
                );
            });
        }

        @Test
        void getCustomerById_invalidIdFormat_returnsInvalidIdFormatException(){
            Assertions.assertThrows(InvalidIdFormatException.class, () -> {
                customerService.getCustomerById("invalidFormat");
            });
        }
    }
}