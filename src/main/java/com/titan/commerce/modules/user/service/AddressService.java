package com.titan.commerce.modules.user.service;

import com.titan.commerce.modules.user.domain.Address;
import com.titan.commerce.modules.user.domain.User;
import com.titan.commerce.modules.user.dto.AddressRequestDTO;
import com.titan.commerce.modules.user.dto.AddressResponseDTO;
import com.titan.commerce.modules.user.repository.AddressRepository;
import com.titan.commerce.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public AddressResponseDTO create(Long userId, AddressRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Address address = dto.toEntity();
        address.setUser(user);
        address.setActive(true);

        return new AddressResponseDTO(repository.save(address));
    }

    public List<AddressResponseDTO> listAllByUser(Long userId) {
        if (!userRepository.existsById(userId)){
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        List<Address> addresses = repository.findByUserIdAndActiveTrue(userId);

        return addresses.stream()
                .map(AddressResponseDTO::new)
                .collect(toList());
    }

    @Transactional
    public void delete(Long addressId, Long userId) {
        Address address = repository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para deletar este endereço");
        }

        address.setActive(false);
        repository.save(address);
    }

}




