package com.titan.commerce.modules.user.dto;

import com.titan.commerce.modules.user.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {
    private Long id;
    private String zipCode;
    private String street;
    private String city;

    public AddressResponseDTO(Address address) {
        this.id = address.getId();
        this.zipCode = address.getZipCode();
        this.street = address.getStreet();
        this.city = address.getCity();
    }
}
