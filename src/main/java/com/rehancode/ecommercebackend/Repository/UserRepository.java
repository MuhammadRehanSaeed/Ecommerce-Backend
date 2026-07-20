package com.rehancode.ecommercebackend.Repository;


import com.rehancode.ecommercebackend.Enum.Roles;
import com.rehancode.ecommercebackend.Model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
    Optional<UserModel> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Page<UserModel> findByRole(Roles role, Pageable pageable);

}
