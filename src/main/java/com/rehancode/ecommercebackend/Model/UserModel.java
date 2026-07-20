package com.rehancode.ecommercebackend.Model;


import com.rehancode.ecommercebackend.Enum.Roles;
import com.rehancode.ecommercebackend.Enum.STATUS;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "Users")
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private STATUS status;
    private boolean enabled;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
