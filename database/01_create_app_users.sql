-- ============================================================
-- MODIFICACIÓN 1: Crear tabla de usuarios de la aplicación
-- Base de datos: northwind
-- ============================================================

CREATE TABLE app_users (
    user_id   SERIAL PRIMARY KEY,
    username  VARCHAR(60)  UNIQUE NOT NULL,
    password  VARCHAR(255) NOT NULL,          -- BCrypt hash
    full_name VARCHAR(120) NOT NULL,
    role      VARCHAR(30)  NOT NULL,          -- Ej: ADMIN, USER
    enabled   BOOLEAN      NOT NULL DEFAULT TRUE
);

-- Índice para búsqueda rápida por username en el login
CREATE INDEX idx_app_users_username ON app_users(username);

COMMENT ON TABLE  app_users             IS 'Usuarios de la aplicación con autenticación BCrypt';
COMMENT ON COLUMN app_users.user_id     IS 'Identificador único del usuario';
COMMENT ON COLUMN app_users.username    IS 'Nombre de usuario único para login';
COMMENT ON COLUMN app_users.password    IS 'Contraseña cifrada con BCrypt (no texto plano)';
COMMENT ON COLUMN app_users.full_name   IS 'Nombre completo del usuario';
COMMENT ON COLUMN app_users.role        IS 'Rol del usuario: ADMIN o USER';
COMMENT ON COLUMN app_users.enabled     IS 'Estado activo (true) o inactivo (false)';
