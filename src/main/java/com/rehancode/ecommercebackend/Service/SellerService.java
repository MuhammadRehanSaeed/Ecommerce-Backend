package com.rehancode.ecommercebackend.Service;

import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface SellerService {
    ApiResponse<ProductAddDTO> addProduct(ProductAddDTO productAddDTO, MultipartFile file);

    ApiResponse<Page<ProductAddDTO>> getProducts(int page, int size);

    ApiResponse<ProductAddDTO> getProductById(Long id);
}
