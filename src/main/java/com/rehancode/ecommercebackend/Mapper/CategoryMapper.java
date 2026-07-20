package com.rehancode.ecommercebackend.Mapper;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.Model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category mapToEntity(CategoryAddDTO categoryAddDTO);

    CategoryAddDTO mapToDTO(Category category);
}
