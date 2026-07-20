package com.rehancode.ecommercebackend.Mapper;

import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.Model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ProfileDTO userToProfileDTO(UserModel user);
}
