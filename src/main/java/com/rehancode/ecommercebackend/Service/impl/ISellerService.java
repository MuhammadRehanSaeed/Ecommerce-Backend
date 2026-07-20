package com.rehancode.ecommercebackend.Service.impl;

import com.rehancode.ecommercebackend.DTO.ProductAddDTO;
import com.rehancode.ecommercebackend.DTO.ProfileDTO;
import com.rehancode.ecommercebackend.DTO.RegisterResponseDTO;
import com.rehancode.ecommercebackend.Enum.ProductStatus;
import com.rehancode.ecommercebackend.Enum.Roles;
import com.rehancode.ecommercebackend.Enum.STATUS;
import com.rehancode.ecommercebackend.Exception.ApiResponse;
import com.rehancode.ecommercebackend.Exception.UserAlreadyExists;
import com.rehancode.ecommercebackend.Mapper.ProductMapper;
import com.rehancode.ecommercebackend.Model.Category;
import com.rehancode.ecommercebackend.Model.Products;
import com.rehancode.ecommercebackend.Model.UserModel;
import com.rehancode.ecommercebackend.Repository.CategoryRepository;
import com.rehancode.ecommercebackend.Repository.ProductRepository;
import com.rehancode.ecommercebackend.Repository.UserRepository;
import com.rehancode.ecommercebackend.Service.ImageService;
import com.rehancode.ecommercebackend.Service.SellerService;
import com.rehancode.ecommercebackend.Utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ISellerService implements SellerService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper  productMapper;
    private final ProductRepository productRepository;
    private final ImageService imageService;
    public ISellerService(ImageService imageService,ProductRepository productRepository,UserRepository userRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
        this.productRepository=productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ApiResponse<ProductAddDTO> addProduct(ProductAddDTO productAddDTO, MultipartFile file) {

        Long userId= SecurityUtils.getAuthenticatedUserId();
        UserModel seller = userRepository.findById(userId)
                .orElseThrow(() -> new UserAlreadyExists("User not found"));
        if(!seller.isEnabled() || seller.getStatus().equals(STATUS.BLOCKED) || seller.getStatus().equals(STATUS.DELETED)){
            throw new UserAlreadyExists("User not allowed to add product");
        }
        if (productAddDTO.getStock() == null || productAddDTO.getStock() < 0) {
            throw new UserAlreadyExists("Stock must be zero or greater");
        }

        if (productAddDTO.getPrice() == null || productAddDTO.getPrice() < 0) {
            throw new UserAlreadyExists("Price must be zero or greater");
        }


        Products product = productMapper.mapToEntity(productAddDTO);

        product.setSeller(seller);

        Category category=categoryRepository.findById(productAddDTO.getCategoryId())
                .orElseThrow(() -> new UserAlreadyExists("Category not found"));

        product.setCategory(category);
        String imageUrl= imageService.uploadImage(file);
        product.setImageUrl(imageUrl);
        product.setStatus(ProductStatus.PENDING);
        productRepository.save(product);

        ProductAddDTO productAddDTO1=productMapper.mapToDto(product);
        productAddDTO1.setCategoryId(product.getCategory().getId());



        return ApiResponse.<ProductAddDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .message("Product Added Success")
                .data(productAddDTO1)
                .build();

    }

    @Override
    public ApiResponse<Page<ProductAddDTO>> getProducts(int page, int size) {
        Long userId= SecurityUtils.getAuthenticatedUserId();
        UserModel seller = userRepository.findById(userId)
                .orElseThrow(() -> new UserAlreadyExists("User not found"));
        Pageable pageable = PageRequest.of(page, size);

        Page<Products> productsPage = productRepository.findBySellerId(userId,pageable);

        Page<ProductAddDTO> productAddDTOS=productsPage.map(
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
    public ApiResponse<ProductAddDTO> getProductById(Long id) {
        Long userId= SecurityUtils.getAuthenticatedUserId();
        UserModel seller = userRepository.findById(userId)
                .orElseThrow(() -> new UserAlreadyExists("User not found"));
        Products productsPage = productRepository.findByIdAndSellerId(id,userId);

        ProductAddDTO productAddDTO=productMapper.mapToDto(productsPage);

        return ApiResponse.<ProductAddDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Product Fetch")
                .data(productAddDTO)
                .build();
    }
}
