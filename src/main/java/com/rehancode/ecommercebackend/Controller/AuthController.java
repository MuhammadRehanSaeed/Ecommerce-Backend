package com.rehancode.ecommercebackend.Controller;


import com.rehancode.ecommercebackend.DTO.LoginRequestDTO;
import com.rehancode.ecommercebackend.DTO.LoginResponseDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Login attempt for email={}", loginRequestDTO.getEmail());
        ApiResponse<LoginResponseDTO> dto = authService.login(loginRequestDTO);
        log.info("Login successful for email={}", loginRequestDTO.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        log.info("Registration attempt for email={}", requestDTO.getEmail());
        ApiResponse<RegisterResponseDTO> dto = authService.register(requestDTO);
        log.info("Registration successful for email={}", requestDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
