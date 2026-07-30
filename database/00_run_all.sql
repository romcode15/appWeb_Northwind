-- ============================================================
-- SCRIPT MAESTRO — Ejecutar en orden sobre la BD northwind
-- ============================================================
-- Paso 1: Crear tabla app_users
\i 01_create_app_users.sql

-- Paso 2: Modificar tabla orders (agregar app_user_id)
\i 02_alter_orders.sql

-- Paso 3: Insertar usuarios de prueba con contraseñas BCrypt
\i 03_seed_app_users.sql
