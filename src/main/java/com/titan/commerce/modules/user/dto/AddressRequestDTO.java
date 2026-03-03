package com.titan.commerce.modules.user.dto;

import com.titan.commerce.modules.user.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {

    @Schema(description = "Código de Endereçamento Postal (CEP)", example = "01310-200")
    @NotBlank
    private String zipCode;

    @Schema(description = "Logradouro (Rua, Avenida, etc.) com número e complemento", example = "Avenida Paulista, 1578 - Apto 42")
    @NotBlank
    private String street;

    @Schema(description = "Nome da cidade", example = "São Paulo")
    @NotBlank
    private String city;

    public Address toEntity(){
        Address newAddress = new Address();
        newAddress.setZipCode(this.zipCode);
        newAddress.setStreet(this.street);
        newAddress.setCity(this.city);
        return newAddress;
    }
}