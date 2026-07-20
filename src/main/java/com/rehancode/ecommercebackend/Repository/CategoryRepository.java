package com.rehancode.ecommercebackend.Repository;

import com.rehancode.ecommercebackend.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
