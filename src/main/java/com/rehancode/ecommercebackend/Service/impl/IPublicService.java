package com.rehancode.ecommercebackend.Service.impl;

import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.Enum.ProductStatus;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Exception.UserAlreadyExists;
import com.rehancode.ecommercebackend.Mapper.ProductMapper;
import com.rehancode.ecommercebackend.Model.Products;
import com.rehancode.ecommercebackend.Repository.ProductRepository;
import com.rehancode.ecommercebackend.Service.PublicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class IPublicService implements PublicService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    public IPublicService(ProductRepository productRepository,ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }
    @Override
    public ApiResponse<Page<ProductAddDTO>> getProducts(int page, int size) {
        Pageable pageable= PageRequest.of(page,size);
//        Page<Products> products=productRepository.findAll(pageable);
        Page<Products> products=productRepository.findByStatus(ProductStatus.APPROVED,pageable);
        if(products == null){
            throw new UserAlreadyExists("Product not found");
        }

        Page<ProductAddDTO> productAddDTOS=products.map(
                logs-> new ProductAddDTO(
                        logs.getName(),
                        logs.getDescription(),
                        logs.getPrice(),
                        logs.getStock(),
                        logs.getCategory().getId()

                )
        );

        return ApiResponse.<Page<ProductAddDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product Fetch")
                .data(productAddDTOS)
                .build();

    }

    @Override
    public ApiResponse<ProductAddDTO> getProductsById(Long id) {
        Products product=productRepository.findByIdAndStatus(id,ProductStatus.APPROVED);
        ProductAddDTO productAddDTO=productMapper.mapToDto(product);
        if(product == null){
            throw new UserAlreadyExists("Product not found");
        }

        return ApiResponse.<ProductAddDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product Fetch")
                .data(productAddDTO)
                .build();

    }

    }
