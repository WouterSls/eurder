package com.switchfully.eurder.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.address.AddressDTO;
import org.springframework.stereotype.Component;

@Component
class AddressMapper {

    AddressDTO mapToDTO(Address address){
        return new AddressDTO(address.getStreet(), address.getHouseNumber(), address.getPostalCode(), address.getCity(), address.getCountry());
    }

    Address mapToDomain(AddressDTO addressDTO){
        return new Address(addressDTO.getStreet(), addressDTO.getHouseNumber(), addressDTO.getPostalCode(), addressDTO.getCity(), addressDTO.getCountry());
    }
}
