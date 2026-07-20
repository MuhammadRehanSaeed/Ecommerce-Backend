package com.rehancode.ecommercebackend.Controller;


import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Service.PublicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/public")
public class PublicController {
    private final PublicService publicService;

    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    @PostMapping("getProduct")
    public ResponseEntity<ApiResponse<Page<ProductAddDTO>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("Public fetching products page={}, size={}", page, size);
        ApiResponse<Page<ProductAddDTO>> resp = publicService.getProducts(page, size);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("getProduct/{id}")
    public ResponseEntity<ApiResponse<ProductAddDTO>> getProductsById(
            @PathVariable Long id
    ) {
        log.debug("Public fetching product id={}", id);
        ApiResponse<ProductAddDTO> resp = publicService.getProductsById(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

}
