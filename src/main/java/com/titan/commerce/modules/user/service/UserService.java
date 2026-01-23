package com.titan.commerce.modules.user.service;

import com.titan.commerce.modules.user.domain.Role;
import com.titan.commerce.modules.user.domain.User;
import com.titan.commerce.modules.user.dto.RegisterDTO;
import com.titan.commerce.modules.user.repository.RoleRepository;
import com.titan.commerce.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User register(RegisterDTO dto){
        if (repository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("Já existe um usuário com esse e-mail");
        }

        Role rolePadrao = roleRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("Role não encontrada"));

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        User user =  new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encryptedPassword);
        user.setActive(true);

        user.getRoles().add(rolePadrao);

        return repository.save(user);
    }

}
