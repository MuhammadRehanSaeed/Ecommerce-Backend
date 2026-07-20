package com.rehancode.ecommercebackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileDTO {

    private String email;
    private String phone;
    private String username;
}
