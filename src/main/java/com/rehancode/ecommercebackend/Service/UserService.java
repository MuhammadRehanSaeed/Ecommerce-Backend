package com.rehancode.ecommercebackend.Service;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.DTO.ChangePasswordDTO;
import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.DTO.UpdateProfileDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import org.springframework.data.domain.Page;

public interface UserService {
    ApiResponse<ProfileDTO> getProfile();

    ApiResponse<String> updateProfile(UpdateProfileDTO profileDTO);

    ApiResponse<String> changePassword(ChangePasswordDTO requestDto);
    ApiResponse<Page<CategoryAddDTO>> getCategory(int page, int size);

    ApiResponse<CategoryAddDTO> getCategoryById(Long id);
}
