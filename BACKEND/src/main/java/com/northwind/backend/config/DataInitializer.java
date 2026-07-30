package com.northwind.backend.config;

import com.northwind.backend.entity.AppUser;
import com.northwind.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea usuarios de prueba al iniciar.
 * Si ya existen, actualiza el password para garantizar que el hash BCrypt sea válido.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        upsertUser("admin",    "admin123", "Administrador del Sistema", "ADMIN", true);
        upsertUser("usuario",  "user123",  "Usuario de Prueba",         "USER",  true);
        upsertUser("inactivo", "test123",  "Usuario Inactivo Test",     "USER",  false);
    }

    private void upsertUser(String username, String rawPassword,
                            String fullName, String role, boolean enabled) {
        AppUser user = appUserRepository.findByUsername(username).orElse(
            AppUser.builder()
                .username(username)
                .fullName(fullName)
                .role(role)
                .enabled(enabled)
                .build()
        );
        // Siempre regenera el hash para garantizar que sea válido
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setRole(role);
        user.setEnabled(enabled);
        appUserRepository.save(user);
        System.out.println("Usuario listo: " + username);
    }
}
