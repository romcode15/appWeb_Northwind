package com.northwind.backend.service;

import com.northwind.backend.dto.LoginRequest;
import com.northwind.backend.dto.LoginResponse;
import com.northwind.backend.entity.AppUser;
import com.northwind.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        // 1. Buscar usuario
        AppUser user = appUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Verificar que esté activo
        if (!user.getEnabled()) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        // 3. Comparar contraseña con BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole()
        );
    }
}
