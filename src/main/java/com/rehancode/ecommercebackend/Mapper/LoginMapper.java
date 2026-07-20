package com.rehancode.ecommercebackend.Mapper;


import com.rehancode.ecommercebackend.DTO.LoginRequestDTO;
import com.rehancode.ecommercebackend.DTO.LoginResponseDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface LoginMapper {
    LoginResponseDTO mapToDto(UserModel usersModel);
    @Mapping(target = "password", ignore = true)
    UserModel mapToUser(RegisterRequestDTO requestDTO);

    RegisterResponseDTO mapToDto1(UserModel usersModel);
}
