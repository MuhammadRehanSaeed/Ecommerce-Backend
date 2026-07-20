package com.rehancode.ecommercebackend.Controller;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.DTO.ChangePasswordDTO;
import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.DTO.UpdateProfileDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("me")
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfile() {
        log.debug("User fetching own profile");
        ApiResponse<ProfileDTO> profileResp = userService.getProfile();
        return ResponseEntity.status(HttpStatus.OK).body(profileResp);
    }

    @PutMapping("update")
    public ResponseEntity<ApiResponse<String>> updateProfile(@RequestBody UpdateProfileDTO profileDTO) {
        log.info("User updating profile");
        ApiResponse<String> updateRes = userService.updateProfile(profileDTO);
        log.info("User profile updated");
        return ResponseEntity.status(HttpStatus.OK).body(updateRes);
    }

    @PostMapping("change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordDTO requestDto) {
        log.info("User changing password");
        ApiResponse<String> updateRes = userService.changePassword(requestDto);
        log.info("User password changed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(updateRes);
    }

    @PostMapping("getCategory")
    public ResponseEntity<ApiResponse<Page<CategoryAddDTO>>> getCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("User fetching categories page={}, size={}", page, size);
        ApiResponse<Page<CategoryAddDTO>> resp = userService.getCategory(page, size);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("getCategory/{id}")
    public ResponseEntity<ApiResponse<CategoryAddDTO>> getCategoryById(@PathVariable Long id) {
        log.debug("User fetching category id={}", id);
        ApiResponse<CategoryAddDTO> resp = userService.getCategoryById(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
