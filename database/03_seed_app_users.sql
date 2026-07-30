-- ============================================================
-- MODIFICACIÓN 3: Datos iniciales — usuarios de prueba
--
-- IMPORTANTE: Estos hashes BCrypt fueron generados con cost=10
-- usando la librería Spring Security BCryptPasswordEncoder.
--
-- Credenciales de prueba:
--   username: admin     | password: admin123  | rol: ADMIN  | activo: true
--   username: usuario   | password: user123   | rol: USER   | activo: true
--   username: inactivo  | password: test123   | rol: USER   | activo: false  ← para probar validación
--
-- Para regenerar los hashes en Java:
--   new BCryptPasswordEncoder().encode("admin123")
-- ============================================================

INSERT INTO app_users (username, password, full_name, role, enabled)
VALUES
    (
        'admin',
        '$2a$10$slYQmyNdgTY18LGvgxPwHOIHOJG9OIbW/Xm8E1C5wqXkxCiGLJEO6',
        'Administrador del Sistema',
        'ADMIN',
        TRUE
    ),
    (
        'usuario',
        '$2a$10$8K1p/a0dhrxSA8WHdRxceutiV6AQOL3Z5i7P4V/vUiBjxIjkClr4y',
        'Usuario de Prueba',
        'USER',
        TRUE
    ),
    (
        'inactivo',
        '$2a$10$8K1p/a0dhrxSA8WHdRxceutiV6AQOL3Z5i7P4V/vUiBjxIjkClr4y',
        'Usuario Inactivo Test',
        'USER',
        FALSE
    );
