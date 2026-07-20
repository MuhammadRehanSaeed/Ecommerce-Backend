package com.rehancode.ecommercebackend.Service.impl;

import com.rehancode.ecommercebackend.DTO.*;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Exception.BadCredentials;
import com.rehancode.ecommercebackend.Exception.UserAlreadyExists;
import com.rehancode.ecommercebackend.Mapper.CategoryMapper;
import com.rehancode.ecommercebackend.Mapper.UserMapper;
import com.rehancode.ecommercebackend.Model.Category;
import com.rehancode.ecommercebackend.Model.UserModel;
import com.rehancode.ecommercebackend.Repository.CategoryRepository;
import com.rehancode.ecommercebackend.Repository.UserRepository;
import com.rehancode.ecommercebackend.Security.UserPrinciple;
import com.rehancode.ecommercebackend.Service.UserService;
import com.rehancode.ecommercebackend.Utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class IUserService implements UserService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public IUserService(CategoryRepository categoryRepository,UserRepository userRepository,UserMapper userMapper,CategoryMapper categoryMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    @Override
    public ApiResponse<ProfileDTO> getProfile() {
        Long userId=SecurityUtils.getAuthenticatedUserId();
        UserModel userModel=userRepository.findById(userId).orElseThrow(()->new UserAlreadyExists("No User Found"));

        ProfileDTO profileDTO=userMapper.userToProfileDTO(userModel);


        return ApiResponse.<ProfileDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User Profile")
                .data(profileDTO)
                .build();
    }

    @Override
    public ApiResponse<String> updateProfile(UpdateProfileDTO profileDTO) {

        Long userId = SecurityUtils.getAuthenticatedUserId();

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new UserAlreadyExists("User not found"));

        // Username update
        if (profileDTO.getUsername() != null &&
                !profileDTO.getUsername().isBlank() &&
                isChanged(userModel.getUsername(), profileDTO.getUsername())) {

            userModel.setUsername(profileDTO.getUsername());
        }

        // Email update
        if (profileDTO.getEmail() != null &&
                !profileDTO.getEmail().isBlank() &&
                isChanged(userModel.getEmail(), profileDTO.getEmail())) {

            userModel.setEmail(profileDTO.getEmail());
        }

        // Phone update
        if (profileDTO.getPhone() != null &&
                !profileDTO.getPhone().isBlank() &&
                isChanged(userModel.getPhone(), profileDTO.getPhone())) {

            userModel.setPhone(profileDTO.getPhone());
        }

        userRepository.save(userModel);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Profile updated successfully")
                .data("Profile updated successfully")
                .build();
    }

    @Override
    public ApiResponse<String> changePassword(ChangePasswordDTO requestDto) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new UserAlreadyExists("User not found"));
        if(!requestDto.getNewPassword().equals(requestDto.getConfirmNewPassword())) {
            throw new BadCredentials("Passwords do not match1");
        }
        if(!encoder.matches(requestDto.getOldPassword(), userModel.getPassword())) {
            throw new BadCredentials("Passwords do not match2");
        }
        if (encoder.matches(requestDto.getNewPassword(), userModel.getPassword())) {

            throw new BadCredentials("New password cannot be same as old password");
        }
        userModel.setPassword(encoder.encode(requestDto.getNewPassword()));
        userRepository.save(userModel);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Changing Password")
                .data("Password changed successfully")
                .build();

    }

    @Override
    public ApiResponse<Page<CategoryAddDTO>> getCategory(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        Page<CategoryAddDTO> categoryAddDTOPage=categoryPage.map(
                logs-> new CategoryAddDTO(
                        logs.getName(),
                        logs.getDescription(),
                        logs.isActive()
                )
        );
        return ApiResponse.<Page<CategoryAddDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Category Fetch")
                .data(categoryAddDTOPage)
                .build();
    }

    @Override
    public ApiResponse<CategoryAddDTO> getCategoryById(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new UserAlreadyExists("No Category exists with id:"+id));
        CategoryAddDTO categoryAddDTO=categoryMapper.mapToDTO(category);
        return ApiResponse.<CategoryAddDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Category Fetched By Id")
                .data(categoryAddDTO)
                .build();

    }


    private boolean isChanged(String oldVal, String newVal) {
        return newVal != null && !Objects.equals(oldVal, newVal);
    }
}
