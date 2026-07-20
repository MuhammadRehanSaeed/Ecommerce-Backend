package com.rehancode.ecommercebackend.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryAddDTO {
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "description is required")
    private String description;

    private boolean active;

    public CategoryAddDTO(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }
}
