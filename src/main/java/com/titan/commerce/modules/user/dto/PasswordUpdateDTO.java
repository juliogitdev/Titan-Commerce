package com.titan.commerce.modules.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateDTO {
    @NotBlank private String oldPassword;
    @NotBlank @Size(min = 8) String newPassword;
}
