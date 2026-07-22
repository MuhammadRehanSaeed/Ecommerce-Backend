package com.rehancode.ecommercebackend.Service.impl;

import com.rehancode.ecommercebackend.DTO.LoginRequestDTO;
import com.rehancode.ecommercebackend.DTO.LoginResponseDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Enum.Roles;
import com.rehancode.ecommercebackend.Enum.STATUS;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Exception.BadCredentials;
import com.rehancode.ecommercebackend.Exception.UserAlreadyExists;
import com.rehancode.ecommercebackend.Exception.UserDisabledException;
import com.rehancode.ecommercebackend.Jwt.JwtService;
import com.rehancode.ecommercebackend.Mapper.LoginMapper;
import com.rehancode.ecommercebackend.Model.UserModel;
import com.rehancode.ecommercebackend.Redis.RedisTokenBlacklistService;
import com.rehancode.ecommercebackend.Repository.UserRepository;
import com.rehancode.ecommercebackend.Security.UserPrinciple;
import com.rehancode.ecommercebackend.Service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class IAuthService implements AuthService {
    private final LoginMapper loginMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTokenBlacklistService redisTokenBlacklistService;
    public IAuthService(LoginMapper loginMapper, UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService, RedisTokenBlacklistService redisTokenBlacklistService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.loginMapper = loginMapper;
        this.redisTokenBlacklistService = redisTokenBlacklistService;
    }
    @Override
    public ApiResponse<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication;
        try{
            authentication=authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken
                            (loginRequestDTO.getUsername(),loginRequestDTO.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentials("Invalid username or password");
        }catch (DisabledException e) {
            throw new UserDisabledException("Your account has been blocked.");
        }
        UserPrinciple userPrinciple=(UserPrinciple)authentication.getPrincipal();
        UserModel userModel=userPrinciple.getPrinciple();
        String token=jwtService.generateToken(userModel);

        LoginResponseDTO loginResponseDTO=loginMapper.mapToDto(userModel);
        loginResponseDTO.setToken(token);

        return ApiResponse.<LoginResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Login Success")
                .data(loginResponseDTO)
                .build();
    }

    @Override
    public ApiResponse<RegisterResponseDTO> register(RegisterRequestDTO requestDTO) {
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

        userModel.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword()));
        userModel.setRole(Roles.CUSTOMER);
        userModel.setEnabled(true);
        userModel.setStatus(STATUS.ACTIVE);
        userRepository.save(userModel);
        System.out.println(userModel.getPhone());

        RegisterResponseDTO responseDTO=loginMapper.mapToDto1(userModel);
        responseDTO.setMessage("Register Success");

        return ApiResponse.<RegisterResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Registration Success")
                .data(responseDTO)
                .build();
    }

    @Override
    public ApiResponse<String> logout(HttpServletRequest request) {
        String authHeader=request.getHeader("Authorization");
        String token=authHeader.substring(7);
        Date exp=jwtService.extractExpiration(token);
        long remainingTime =
                exp.getTime() - System.currentTimeMillis();
        redisTokenBlacklistService.blacklist(token,remainingTime);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Logout Success")
                .data("Success")
                .build();
    }

}
