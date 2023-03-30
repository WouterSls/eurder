package com.switchfully.eurder.components.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
//import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.components.CustomerComponent.Customer;
import com.switchfully.eurder.components.CustomerComponent.CustomerMapper;
import com.switchfully.eurder.components.CustomerComponent.CustomerRepository;
import com.switchfully.eurder.components.CustomerComponent.CustomerService;
import com.switchfully.eurder.exception.InvalidIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;


class CustomerServiceTest {

    @Nested
    @DisplayName("CustomerService unit test")
    class unitTestMocking {

        private CustomerService customerService;
        private CustomerRepository customerRepoMock;
        private CustomerMapper customerMapperMock;


        private final Customer TEST_CUSTOMER = new Customer("Foo", "Bar", "foobar@email.com", "fooStreetBar", "0032foo456bar");
        private final CreateCustomerDTO TEST_CUSTOMER_CREATE = new CreateCustomerDTO("Create", "Customer", "createdCustomer@email.com", "createdStreetCustomer", "0031create456customer");

        @BeforeEach
        void setup() {
            customerRepoMock = Mockito.mock(CustomerRepository.class);
            customerMapperMock = Mockito.mock(CustomerMapper.class);
            customerService = new CustomerService(customerRepoMock, customerMapperMock);
        }

        @Test
        void getCustomerByFirstName_CustomersPresent_returnsCustomers() {
            Mockito.when(customerRepoMock.getCustomerByName(TEST_CUSTOMER.getFirstName()))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            CustomerDTO actualCustomer = customerService.getCustomerByName(TEST_CUSTOMER.getFirstName());

            Assertions.assertEquals(customerMapperMock.mapToDTO(TEST_CUSTOMER), actualCustomer);
        }

        @Test
        void getCustomerByLastName_CustomersPresent_returnsCustomers() {
            Mockito.when(customerRepoMock.getCustomerByName(TEST_CUSTOMER.getLastName()))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            CustomerDTO actualCustomer = customerService.getCustomerByName(TEST_CUSTOMER.getLastName());

            Assertions.assertEquals(customerMapperMock.mapToDTO(TEST_CUSTOMER), actualCustomer);
        }

        @Test
        void getCustomerByName_CustomersNotPresent_returnsNull() {
            CustomerDTO actualCustomer = customerService.getCustomerByName(TEST_CUSTOMER.getLastName());

            Assertions.assertNull(actualCustomer);
        }

        @Test
        void getCustomerByName_CustomerPresentIncorrectName_returnsNull() {
            Mockito.when(customerRepoMock.getCustomerByName(TEST_CUSTOMER.getFirstName()))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            CustomerDTO actualCustomer = customerService.getCustomerByName("incorrectName");

            Assertions.assertNull(actualCustomer);
        }

        @Test
        void getListCustomerDTO_noCustomers_returnsEmptyList() {
            Mockito.when(customerRepoMock.getCustomers())
                    .thenReturn(emptyList());

            List<CustomerDTO> actualList = customerService.getListCustomerDTO();

            Assertions.assertTrue(actualList.isEmpty());
        }

        @Test
        void getListCustomerDTO_CustomersPresent_returnsListOfCustomers() {
            Mockito.when(customerRepoMock.getCustomers())
                    .thenReturn(List.of(TEST_CUSTOMER));

            List<CustomerDTO> actualList = customerService.getListCustomerDTO();

            Assertions.assertEquals(customerMapperMock.mapToDTO(List.of(TEST_CUSTOMER)), actualList);
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPresent_returnsCustomerDTO() {
            customerService.createNewCustomer(TEST_CUSTOMER_CREATE);

            Mockito.verify(customerRepoMock).addCustomer(customerMapperMock.mapToDomain(TEST_CUSTOMER_CREATE));
            Mockito.verify(customerRepoMock, Mockito.never()).getCustomerByName("foo");
            Mockito.verify(customerRepoMock, Mockito.never()).getCustomers();
        }

        @Test
        void createNewCustomer_CreateCustomerDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(null);
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOFirstNameNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(new CreateCustomerDTO(
                        null,
                        "lastName",
                        "emailAddress",
                        "address",
                        "phoneNumber")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOLastNameNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(new CreateCustomerDTO(
                        "firstName",
                        null,
                        "emailAddress",
                        "address",
                        "phoneNumber")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOEmailAddressNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(new CreateCustomerDTO(
                        "firstName",
                        "lastName",
                        null,
                        "address",
                        "phoneNumber")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOAddressNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(new CreateCustomerDTO(
                        "firstName",
                        "lastName",
                        "emailAddress",
                        null,
                        "phoneNumber")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPhoneNumberPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(new CreateCustomerDTO(
                        "firstName",
                        "lastName",
                        "emailAddress",
                        "address",
                        null)
                );
            });
        }

        @Test
        void getCustomerById_CustomerPresent_returnsCustomersDTO() {
            Mockito.when(customerRepoMock.getCustomerById(TEST_CUSTOMER.getId()))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            CustomerDTO actualCustomer = customerService.getCustomerById(TEST_CUSTOMER.getId().toString());

            Assertions.assertEquals(customerMapperMock.mapToDTO(TEST_CUSTOMER), actualCustomer);
        }

        @Test
        void getCustomerById_CustomerNotPresent_returnsNull() {
            Mockito.when(customerRepoMock.getCustomerById(TEST_CUSTOMER.getId()))
                    .thenReturn(null);

            Assertions.assertNull(customerService.getCustomerById(UUID.randomUUID().toString()));
        }

        @Test
        void getCustomerById_CustomerPresentIncorrectId_returnsInvalidIdException() {

            Assertions.assertThrows(InvalidIdException.class, () -> {
                customerService.getCustomerById("foo");
            });
        }
    }

    @Nested
    @DisplayName("CustomerService Integration test")
    class CustomerServiceIntegrationTest{
        //TODO: Integration test
    }
}