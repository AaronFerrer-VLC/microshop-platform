-- Migración Flyway: Crear tabla de productos
-- Versión: 1
-- Descripción: Crea la tabla products con todos los campos necesarios

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    price NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL,
    category VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice para búsquedas por categoría
CREATE INDEX IF NOT EXISTS idx_product_category ON products(category);

-- Comentarios en las columnas para documentación
COMMENT ON TABLE products IS 'Tabla que almacena el catálogo de productos';
COMMENT ON COLUMN products.id IS 'Identificador único del producto';
COMMENT ON COLUMN products.name IS 'Nombre del producto';
COMMENT ON COLUMN products.description IS 'Descripción detallada del producto';
COMMENT ON COLUMN products.price IS 'Precio del producto (máximo 10 dígitos, 2 decimales)';
COMMENT ON COLUMN products.stock IS 'Cantidad disponible en inventario';
COMMENT ON COLUMN products.category IS 'Categoría del producto para filtrado y búsqueda';
COMMENT ON COLUMN products.created_at IS 'Fecha y hora de creación del registro';

-- Constraint para asegurar que el precio sea positivo
ALTER TABLE products ADD CONSTRAINT chk_price_positive CHECK (price > 0);

-- Constraint para asegurar que el stock no sea negativo
ALTER TABLE products ADD CONSTRAINT chk_stock_non_negative CHECK (stock >= 0);

