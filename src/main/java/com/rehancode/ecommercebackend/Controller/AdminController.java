
package com.rehancode.ecommercebackend.Controller;

import com.rehancode.ecommercebackend.DTO.CategoryAddDTO;
import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.DTO.RegisterRequestDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Service.AdminService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @PostMapping("addCategory")
    public ResponseEntity<ApiResponse<String>> addCategory(@Valid @RequestBody CategoryAddDTO request) {
        log.info("Admin adding category name={}", request.getName());
        ApiResponse<String> response = adminService.addCategory(request);
        log.info("Category added successfully name={}", request.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("updateCategory/{id}")
    public ResponseEntity<ApiResponse<String>> updateCategory(@PathVariable Long id, @RequestBody CategoryAddDTO request) {
        log.info("Admin updating category id={}", id);
        ApiResponse<String> response = adminService.updateCategory(id, request);
        log.info("Category updated successfully id={}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("registerSeller")
    public ResponseEntity<ApiResponse<RegisterResponseDTO>> createSeller(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Admin creating seller account email={}", request.getEmail());
        ApiResponse<RegisterResponseDTO> response = adminService.createSeller(request);
        log.info("Seller account created email={}", request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("getSellers")
    public ResponseEntity<ApiResponse<Page<ProfileDTO>>> getSeller(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("Admin fetching sellers page={}, size={}", page, size);
        ApiResponse<Page<ProfileDTO>> resp = adminService.getSellers(page, size);
        return new ResponseEntity<>(resp, HttpStatus.OK);

    }

    @PostMapping("getSeller/{id}")
    public ResponseEntity<ApiResponse<ProfileDTO>> getSellerById(@PathVariable Long id) {
        log.debug("Admin fetching seller id={}", id);
        ApiResponse<ProfileDTO> resp = adminService.getSellerById(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("blockSeller/{id}")
    public ResponseEntity<ApiResponse<String>> blockSeller(@PathVariable Long id) {
        log.warn("Admin blocking seller id={}", id);
        ApiResponse<String> response = adminService.blockSeller(id);
        log.info("Seller blocked id={}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("unblockSeller/{id}")
    public ResponseEntity<ApiResponse<String>> unblockSeller(@PathVariable Long id) {
        log.info("Admin unblocking seller id={}", id);
        ApiResponse<String> response = adminService.unblockSeller(id);
        log.info("Seller unblocked id={}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("deleteSeller/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSeller(@PathVariable Long id) {
        log.warn("Admin deleting seller id={}", id);
        ApiResponse<String> response = adminService.deleteSeller(id);
        log.info("Seller deleted id={}", id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
