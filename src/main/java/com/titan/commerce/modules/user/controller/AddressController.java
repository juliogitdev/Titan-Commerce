package com.titan.commerce.modules.user.controller;

import com.titan.commerce.modules.user.dto.AddressRequestDTO;
import com.titan.commerce.modules.user.dto.AddressResponseDTO;
import com.titan.commerce.modules.user.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @PostMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<AddressResponseDTO> create(@PathVariable Long userId, @RequestBody @Valid AddressRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(userId, dto));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<List<AddressResponseDTO>> list(@PathVariable Long userId) {
        return ResponseEntity.ok(service.listAllByUser(userId));
    }

    @DeleteMapping("/{addressId}/user/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<Void> delete(@PathVariable Long addressId, @PathVariable Long userId) {
        service.delete(addressId, userId);
        return ResponseEntity.noContent().build();
    }
}

