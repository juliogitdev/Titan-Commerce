package com.titan.commerce.modules.user.controller;

import com.titan.commerce.infra.security.TokenService;
import com.titan.commerce.modules.user.domain.User;
import com.titan.commerce.modules.user.dto.AuthenticationDTO;
import com.titan.commerce.modules.user.dto.LoginResponseDTO;
import com.titan.commerce.modules.user.dto.RegisterDTO;
import com.titan.commerce.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints públicos para registro de usuários e geração de tokens de acesso (Login)")
public class AuthenticationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de cliente no e-commerce.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou e-mail já existente no sistema")
    })
    @SecurityRequirements() // <--- Remove o cadeado de segurança desta rota específica!
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO dto) {
        this.userService.register(dto);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Realizar Login", description = "Autentica as credenciais do usuário e devolve um token JWT para uso nas rotas protegidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso. Retorna o Token JWT."),
            @ApiResponse(responseCode = "403", description = "Credenciais inválidas (E-mail ou senha incorretos)")
    })
    @SecurityRequirements() // <--- Remove o cadeado de segurança desta rota específica!
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}