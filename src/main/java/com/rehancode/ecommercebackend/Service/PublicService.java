package com.rehancode.ecommercebackend.Service;

import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import org.springframework.data.domain.Page;

public interface PublicService {
    ApiResponse<Page<ProductAddDTO>> getProducts(int page, int size);

    ApiResponse<ProductAddDTO> getProductsById(Long id);
}
