package com.rehancode.ecommercebackend.DTO;


import lombok.Data;

@Data
public class ProfileDTO {
    private String username;
    private String email;
    private String role;
    private String phone;

    public ProfileDTO(String username, String email, String role, String phone) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.phone = phone;
    }
}
