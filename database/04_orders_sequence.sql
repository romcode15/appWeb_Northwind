-- ============================================================
-- MODIFICACIÓN 4: Crear secuencia para orders.order_id
-- Northwind original no tiene autoincremento en order_id.
-- Se crea una secuencia que parte del valor máximo actual + 1
-- para no colisionar con los datos existentes.
-- ============================================================

-- Crear la secuencia
CREATE SEQUENCE orders_order_id_seq
    START WITH 20000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Asignar la secuencia como valor por defecto de order_id
ALTER TABLE orders
    ALTER COLUMN order_id SET DEFAULT nextval('orders_order_id_seq');

-- Cambiar tipo a INTEGER para compatibilidad con JPA
ALTER TABLE orders
    ALTER COLUMN order_id TYPE INTEGER;
