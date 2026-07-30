-- ============================================================
-- MODIFICACIÓN 5: Crear secuencia para products.product_id
-- La tabla products de Northwind no tiene autoincremento.
-- Se crea una secuencia que parte del valor máximo actual + 1
-- para no colisionar con los datos existentes.
-- ============================================================

-- Crear la secuencia
CREATE SEQUENCE products_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Ajustar el inicio al valor máximo actual + 1
SELECT setval('products_product_id_seq', (SELECT MAX(product_id) FROM products));

-- Asignar la secuencia como valor por defecto de product_id
ALTER TABLE products
    ALTER COLUMN product_id SET DEFAULT nextval('products_product_id_seq');
