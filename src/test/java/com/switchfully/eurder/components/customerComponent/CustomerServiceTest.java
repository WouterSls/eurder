package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.exception.NoCustomersException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;


class CustomerServiceTest {

    @Nested
    @DisplayName("CustomerService unit test")
    class unitTestMocking {

        private CustomerService customerService;
        private ICustomerRepository customerRepoMock;
        private CustomerMapper customerMapperMock;


        private final Customer TEST_CUSTOMER = new Customer(UUID.randomUUID(), "foo","bar", "foobar@email.com", "fooStreetBar", "0032foo456bar");
        private final CreateCustomerDTO TEST_CUSTOMER_CREATE = new CreateCustomerDTO("begijnhof","04123456");

        Jwt jwt = new Jwt("test",Instant.now(),Instant.now().plusSeconds(300),null,null);

        @BeforeEach
        void setup() {
            customerMapperMock = Mockito.mock(CustomerMapper.class);
            customerRepoMock = Mockito.mock(ICustomerRepository.class);
            customerService = new CustomerService(customerMapperMock, customerRepoMock);
        }

        @Test
        void getCustomerById_CustomersPresent_returnsCustomer() {
            Mockito.when(customerRepoMock.findById(TEST_CUSTOMER.getId()))
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

        @Test
        void createNewCustomer_CreateCustomerDTOPresent_returnsCustomer() {

            customerService.createNewCustomer(jwt,TEST_CUSTOMER_CREATE);

            Mockito.verify(customerRepoMock).save(customerMapperMock.mapToDomain(jwt,TEST_CUSTOMER_CREATE));
        }


        @Test
        void createNewCustomer_CreateCustomerDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,null);
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOFirstNameNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,new CreateCustomerDTO(
                        null,
                        "041234567")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOLastNameNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,new CreateCustomerDTO(
                        "firstName",
                        null)
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOEmailAddressNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,new CreateCustomerDTO(
                        "firstName",
                        "lastName")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOAddressNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,new CreateCustomerDTO(
                        "firstName",
                        "lastName")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPhoneNumberPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,new CreateCustomerDTO(
                        "firstName",
                        "lastName")
                );
            });
        }

        @Test
        void createNewCustomer_CreateCustomerDTOPasswordNotPresent_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                customerService.createNewCustomer(jwt,new CreateCustomerDTO(
                        "firstName",
                        "lastName")
                );
            });
        }
    }
}