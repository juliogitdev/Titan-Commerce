package com.titan.commerce.modules.user.repository;

import com.titan.commerce.modules.catalog.domain.Product;
import com.titan.commerce.modules.catalog.domain.ProductVariant;
import com.titan.commerce.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByActiveTrue();
    List<User> findByActiveFalse();
}