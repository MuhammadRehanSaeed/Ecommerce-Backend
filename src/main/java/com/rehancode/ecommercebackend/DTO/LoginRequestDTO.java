package com.rehancode.ecommercebackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

@NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "password is required")
private String password;
}
