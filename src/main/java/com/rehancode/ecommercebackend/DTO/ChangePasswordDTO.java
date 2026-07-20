package com.rehancode.ecommercebackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotBlank(message = "old password cannot be empty")
    private String oldPassword;
    @NotBlank(message = "new password cannot be empty")
    private String newPassword;
    @NotBlank(message = "new password cannot be empty")
    private String confirmNewPassword;
}
