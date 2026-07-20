package com.rehancode.ecommercebackend.Service.impl;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Enum.Roles;
import com.rehancode.ecommercebackend.Enum.STATUS;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Exception.BadCredentials;
import com.rehancode.ecommercebackend.Exception.UserAlreadyExists;
import com.rehancode.ecommercebackend.Mapper.CategoryMapper;
import com.rehancode.ecommercebackend.Mapper.LoginMapper;
import com.rehancode.ecommercebackend.Mapper.UserMapper;
import com.rehancode.ecommercebackend.Model.Category;
import com.rehancode.ecommercebackend.Model.UserModel;
import com.rehancode.ecommercebackend.Repository.CategoryRepository;
import com.rehancode.ecommercebackend.Repository.UserRepository;
import com.rehancode.ecommercebackend.Service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class IAdminService implements AdminService {
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LoginMapper loginMapper;
    private final UserMapper userMapper;
    public IAdminService(UserMapper userMapper,LoginMapper loginMapper,UserRepository userRepository,CategoryMapper categoryMapper,CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.loginMapper = loginMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public ApiResponse<String> addCategory(CategoryAddDTO request) {
        System.out.println("isActive = " + request.isActive());

        Category category = categoryMapper.mapToEntity(request);

        System.out.println("Entity active = " + category.isActive());

        categoryRepository.save(category);
        return ApiResponse.<String>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Category")
                .data("Category added successfully")
                .build();


    }

    @Override
    public ApiResponse<String> updateCategory(Long id, CategoryAddDTO request) {
        Category category=categoryRepository.findById(id).orElseThrow(()->new UserAlreadyExists("No Category exists with id:"+id));
        // name update
        if (request.getName() != null &&
                !request.getName().isBlank() &&
                isChanged(category.getName(), request.getName())) {

            category.setName(request.getName());
        }

        // desc update
        if (request.getDescription() != null &&
                !request.getDescription().isBlank() &&
                isChanged(category.getDescription(), request.getDescription())) {

            category.setDescription(request.getDescription());
        }

        // active update
        if (category.isActive() != request.isActive()) {
            category.setActive(request.isActive());
        }

        categoryRepository.save(category);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Category")
                .data("Category Updated successfully")
                .build();
    }

    @Override
    public ApiResponse<RegisterResponseDTO> createSeller(RegisterRequestDTO requestDTO) {
        if(requestDTO.getEmail()==null || requestDTO.getPassword()==null){
            throw new BadCredentials("Email or password is null");
        }
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new UserAlreadyExists("Email already exists");
        }
        if(userRepository.existsByUsername(requestDTO.getUsername())){
            throw new UserAlreadyExists("Username already exists");
        }
        System.out.println(requestDTO.getPhone());
        UserModel userModel=loginMapper.mapToUser(requestDTO);

        userModel.setPassword(encoder.encode(requestDTO.getPassword()));
        userModel.setRole(Roles.SELLER);
        userModel.setEnabled(true);
        userModel.setStatus(STATUS.ACTIVE);
        userRepository.save(userModel);
        System.out.println(userModel.getPhone());

        RegisterResponseDTO responseDTO=loginMapper.mapToDto1(userModel);
        responseDTO.setMessage("Register Success");

        return ApiResponse.<RegisterResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Seller Registration Success")
                .data(responseDTO)
                .build();
    }

    @Override
    public ApiResponse<Page<ProfileDTO>> getSellers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserModel> userPage = userRepository.findByRole(Roles.SELLER,pageable);

        Page<ProfileDTO> userPageDTO=userPage.map(
                logs-> new ProfileDTO(
                        logs.getUsername(),
                        logs.getEmail(),
                        logs.getRole().name(),
                        logs.getPhone()

                )
        );
        return ApiResponse.<Page<ProfileDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User Fetch")
                .data(userPageDTO)
                .build();

    }

    @Override
    public ApiResponse<ProfileDTO> getSellerById(Long id) {
        UserModel userModel=userRepository.findById(id).orElseThrow(()->new UserAlreadyExists("No User exists with id:"+id));
        if (userModel.getRole() != Roles.SELLER) {
            throw new UserAlreadyExists("No seller exists with id: " + id);
        }

        ProfileDTO profileDTO=userMapper.userToProfileDTO(userModel);
        return ApiResponse.<ProfileDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Seller Fetched By Id")
                .data(profileDTO)
                .build();

    }

    @Override
    public ApiResponse<String> blockSeller(Long id) {
        UserModel userModel=userRepository.findById(id).orElseThrow(()->new UserAlreadyExists("No User exists"));

        userModel.setEnabled(false);
        userRepository.save(userModel);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User Blocked")
                .data("Seller Blocked successfully")
                .build();
    }

    @Override
    public ApiResponse<String> unblockSeller(Long id) {
        UserModel userModel=userRepository.findById(id).orElseThrow(()->new UserAlreadyExists("No User exists"));

        userModel.setEnabled(true);
        userRepository.save(userModel);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User Unblocked")
                .data("Seller Unblocked successfully")
                .build();
    }

    @Override
    public ApiResponse<String> deleteSeller(Long id) {
        UserModel userModel=userRepository.findById(id).orElseThrow(()->new UserAlreadyExists("No User exists"));

        userModel.setEnabled(false);
        userModel.setStatus(STATUS.DELETED);
        userRepository.save(userModel);

        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("User deleted")
                .data("Seller deleted successfully")
                .build();
    }

    private boolean isChanged(String oldVal, String newVal) {
        return newVal != null && !Objects.equals(oldVal, newVal);
    }
}
