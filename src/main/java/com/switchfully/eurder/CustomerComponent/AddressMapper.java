package com.switchfully.eurder.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.address.AddressDTO;

class AddressMapper {

    AddressDTO mapToDTO(Address address){
        return new AddressDTO(address.getStreet(), address.getHouseNumber(), address.getPostalCode(), address.getCity(), address.getCountry());
    }
}
