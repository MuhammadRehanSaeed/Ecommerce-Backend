package com.rehancode.ecommercebackend.Repository;


import com.rehancode.ecommercebackend.Enum.ProductStatus;
import com.rehancode.ecommercebackend.Model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {
    Page<Products> findBySellerId(Long sellerId, Pageable pageable);

    Products findByIdAndSellerId(Long productId,Long sellerId);

    Page<Products> findByStatus(ProductStatus status, Pageable pageable);

    Products findByIdAndStatus(Long productId,ProductStatus status);
}
