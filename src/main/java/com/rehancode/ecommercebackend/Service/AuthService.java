package com.rehancode.ecommercebackend.Service;

import com.rehancode.ecommercebackend.DTO.LoginRequestDTO;
import com.rehancode.ecommercebackend.DTO.LoginResponseDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import jakarta.validation.Valid;

public interface AuthService {
    ApiResponse<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);

    ApiResponse<RegisterResponseDTO> register(@Valid RegisterRequestDTO requestDTO);
}
