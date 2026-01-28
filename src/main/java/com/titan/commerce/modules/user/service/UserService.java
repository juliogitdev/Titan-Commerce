package com.titan.commerce.modules.user.service;

import com.titan.commerce.modules.user.domain.Role;
import com.titan.commerce.modules.user.domain.User;
import com.titan.commerce.modules.user.dto.RegisterDTO;
import com.titan.commerce.modules.user.dto.UserResponseDTO;
import com.titan.commerce.modules.user.dto.UserUpdateDTO;
import com.titan.commerce.modules.user.repository.RoleRepository;
import com.titan.commerce.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
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

    @Transactional
    public UserResponseDTO findById(Long id){
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return new UserResponseDTO(user);
    }

    @Transactional
    public List<UserResponseDTO> findAll(Boolean active){
        List<User> users;

        if (active == null || active){
            users = repository.findByActiveTrue();
        } else {
            users = repository.findByActiveFalse();
        }

        return users
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (dto.getName() != null && !dto.getName().isBlank()){
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()){
            if (!dto.getEmail().equals(user.getEmail()) && repository.existsByEmail(dto.getEmail())){
                throw new IllegalArgumentException("Já existe um usuário com esse e-mail");
            }

            user.setEmail(dto.getEmail());
        }

        return new UserResponseDTO(repository.save(user));

    }

    @Transactional
    public Boolean delete(Long id){
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setActive(false);
        repository.save(user);
        return true;
    }

    @Transactional
    public UserResponseDTO activate(Long id){
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (user.isActive()){
            throw new IllegalArgumentException("Usuário já está ativado");
        }

        user.setActive(true);
        repository.save(user);
        return new UserResponseDTO(user);
    }

}
