package com.rehancode.ecommercebackend.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductAddDTO {

    @NotBlank(message = "Product name cannot be blank")
    private String name;
    @NotBlank(message = "Product description cannot be blank")
    private String description;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Stock cannot be null")
    private Integer stock;

    @NotNull(message = "CategoryId cannot be null")
    private Long categoryId;

    public ProductAddDTO(String name, String description, Double price, Integer stock, Long id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = id;
    }
}
