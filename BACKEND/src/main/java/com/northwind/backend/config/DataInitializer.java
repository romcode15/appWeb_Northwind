package com.northwind.backend.config;

import com.northwind.backend.entity.AppUser;
import com.northwind.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inserta usuarios de prueba al iniciar la aplicación si no existen.
 * Esto garantiza que los hashes BCrypt sean generados correctamente en runtime.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfNotExists("admin",    "admin123", "Administrador del Sistema", "ADMIN", true);
        createUserIfNotExists("usuario",  "user123",  "Usuario de Prueba",         "USER",  true);
        createUserIfNotExists("inactivo", "test123",  "Usuario Inactivo Test",     "USER",  false);
    }

    private void createUserIfNotExists(String username, String rawPassword,
                                        String fullName, String role, boolean enabled) {
        if (appUserRepository.findByUsername(username).isEmpty()) {
            AppUser user = AppUser.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .fullName(fullName)
                    .role(role)
                    .enabled(enabled)
                    .build();
            appUserRepository.save(user);
            System.out.println("Usuario creado: " + username);
        }
    }
}
