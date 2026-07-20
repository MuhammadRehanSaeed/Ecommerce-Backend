package com.rehancode.ecommercebackend.Mapper;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.Model.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seller", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Products mapToEntity(ProductAddDTO productAddDTO);

    ProductAddDTO mapToDto(Products products);
}
