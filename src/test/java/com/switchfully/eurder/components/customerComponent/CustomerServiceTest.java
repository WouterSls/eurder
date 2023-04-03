package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.exception.NoCustomersException;
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


        private final Customer TEST_CUSTOMER = new Customer("Foo", "Bar", "foobar@email.com", "fooStreetBar", "0032foo456bar", "test", Role.CUSTOMER);
        private final CreateCustomerDTO TEST_CUSTOMER_CREATE = new CreateCustomerDTO("Create", "Customer", "createdCustomer@email.com", "createdStreetCustomer", "0031create456customer", "test");

        @BeforeEach
        void setup() {
            customerRepoMock = Mockito.mock(CustomerRepository.class);
            customerMapperMock = Mockito.mock(CustomerMapper.class);
            customerService = new CustomerService(customerRepoMock, customerMapperMock);
        }

        @Test
        void getCustomerById_CustomersPresent_returnsCustomer() {
            Mockito.when(customerRepoMock.getCustomerById(TEST_CUSTOMER.getId()))
                    .thenReturn(Optional.of(TEST_CUSTOMER));

            CustomerDTO actualCustomer = customerService.getCustomerById(TEST_CUSTOMER.getId().toString());

            Assertions.assertEquals(customerMapperMock.mapToDTO(TEST_CUSTOMER), actualCustomer);
        }

        @Test
        void getCustomerId_CustomersNotPresent_returnsIllegalIdException() {
            Assertions.assertThrows(IllegalIdException.class, () -> {
                customerService.getCustomerById(TEST_CUSTOMER.getId().toString());
            });

        }

        @Test
        void getCustomerId_CustomerPresentIncorrectId_returnsIllegalIdException() {
            Mockito.when(customerRepoMock.getCustomerById(TEST_CUSTOMER.getId()))
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
            Mockito.when(customerRepoMock.getCustomers())
                    .thenReturn(emptyList());

            Assertions.assertThrows(NoCustomersException.class, () -> {
                customerService.getListCustomerDTO();
            });
        }

        @Test
        void getListCustomerDTO_CustomersPresent_returnsListOfCustomers() {
            Mockito.when(customerRepoMock.getCustomers())
                    .thenReturn(List.of(TEST_CUSTOMER));

            List<CustomerDTO> actualList = customerService.getListCustomerDTO();

            Assertions.assertEquals(customerMapperMock.mapToDTO(List.of(TEST_CUSTOMER)), actualList);
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPresent_returnsCustomer() {
            customerService.createNewCustomer(TEST_CUSTOMER_CREATE);

            Mockito.verify(customerRepoMock).addCustomer(customerMapperMock.mapToDomain(TEST_CUSTOMER_CREATE));
            Mockito.verify(customerRepoMock, Mockito.never()).getCustomerById(TEST_CUSTOMER.getId());
            Mockito.verify(customerRepoMock, Mockito.never()).getCustomers();
        }

        @Test
        void createNewAdmin_CreateCustomerDTOPresent_returnsCustomer() {
            customerService.createNewAdmin(TEST_CUSTOMER_CREATE);

            Mockito.verify(customerRepoMock).addCustomer(customerMapperMock.mapToDomain(TEST_CUSTOMER_CREATE));
            Mockito.verify(customerRepoMock, Mockito.never()).getCustomerById(TEST_CUSTOMER.getId());
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
                        "phoneNumber",
                        "test")
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
                        "phoneNumber",
                        "test")
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
                        "phoneNumber",
                        "test")
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
                        "phoneNumber",
                        "test")
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
                        null,
                        "test")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPasswordNotPresent_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(new CreateCustomerDTO(
                        "firstName",
                        "lastName",
                        "emailAddress",
                        "address",
                        "phoneNumber",
                        null)
                );
            });
        }
    }
}