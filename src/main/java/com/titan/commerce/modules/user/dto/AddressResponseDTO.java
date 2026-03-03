package com.titan.commerce.modules.user.dto;

import com.titan.commerce.modules.user.domain.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {

    @Schema(description = "ID único interno do endereço", example = "89")
    private Long id;

    @Schema(description = "Código de Endereçamento Postal (CEP)", example = "01310-200")
    private String zipCode;

    @Schema(description = "Logradouro completo", example = "Avenida Paulista, 1578 - Apto 42")
    private String street;

    @Schema(description = "Nome da cidade", example = "São Paulo")
    private String city;

    public AddressResponseDTO(Address address) {
        this.id = address.getId();
        this.zipCode = address.getZipCode();
        this.street = address.getStreet();
        this.city = address.getCity();
    }
}