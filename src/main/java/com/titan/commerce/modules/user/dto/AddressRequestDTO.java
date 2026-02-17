package com.titan.commerce.modules.user.dto;

import com.titan.commerce.modules.user.domain.Address;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {
    @NotBlank private String zipCode;
    @NotBlank private String street;
    @NotBlank private String city;

    public Address toEntity(){
        Address newAddress = new Address();
        newAddress.setZipCode(this.zipCode);
        newAddress.setStreet(this.street);
        newAddress.setCity(this.city);
        return newAddress;
    }
}


