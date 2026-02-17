package com.titan.commerce.modules.user.repository;

import com.titan.commerce.modules.user.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserIdAndActiveTrue(Long userId);
}
