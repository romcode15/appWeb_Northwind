-- ============================================================
-- MODIFICACIÓN 2: Agregar relación entre orders y app_users
-- La tabla orders de Northwind no tiene referencia al usuario
-- de la aplicación que registra la orden.
-- ============================================================

ALTER TABLE orders
    ADD COLUMN app_user_id INTEGER;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_app_user
    FOREIGN KEY (app_user_id)
    REFERENCES app_users(user_id);

-- Índice para consultas por usuario
CREATE INDEX idx_orders_app_user_id ON orders(app_user_id);

COMMENT ON COLUMN orders.app_user_id IS 'Usuario de la aplicación responsable del registro de la orden';
