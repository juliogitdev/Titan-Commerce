package com.titan.commerce.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank private String name;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 8) private String password;
}
