package com.rehancode.ecommercebackend.Controller;


import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Service.SellerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("api/seller")
public class SellerController {
    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping(
            value = "/addProduct",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<ProductAddDTO>> addProduct(
            @Valid @RequestPart("product") ProductAddDTO productAddDTO,
            @RequestPart("file") MultipartFile file) {

        log.info("Seller adding product name={}, price={}, stock={}",
                productAddDTO.getName(), productAddDTO.getPrice(), productAddDTO.getStock());
        ApiResponse<ProductAddDTO> resp = sellerService.addProduct(productAddDTO, file);
        log.info("Product added successfully name={}", productAddDTO.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("getProduct")
    public ResponseEntity<ApiResponse<Page<ProductAddDTO>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("Seller fetching products page={}, size={}", page, size);
        ApiResponse<Page<ProductAddDTO>> resp = sellerService.getProducts(page, size);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping("getProduct/{id}")
    public ResponseEntity<ApiResponse<ProductAddDTO>> getProductsById(
            @PathVariable Long id
    ) {
        log.debug("Seller fetching product id={}", id);
        ApiResponse<ProductAddDTO> resp = sellerService.getProductById(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


}
