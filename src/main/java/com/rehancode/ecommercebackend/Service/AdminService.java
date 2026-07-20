package com.rehancode.ecommercebackend.Service;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface AdminService {
    ApiResponse<String> addCategory(@Valid CategoryAddDTO request);

    ApiResponse<String> updateCategory(Long id, CategoryAddDTO request);


    ApiResponse<RegisterResponseDTO> createSeller(@Valid RegisterRequestDTO request);

    ApiResponse<Page<ProfileDTO>> getSellers(int page, int size);

    ApiResponse<ProfileDTO> getSellerById(Long id);

    ApiResponse<String> blockSeller(Long id);

    ApiResponse<String> unblockSeller(Long id);

    ApiResponse<String> deleteSeller(Long id);
}
