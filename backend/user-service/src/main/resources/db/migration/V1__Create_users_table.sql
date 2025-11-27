-- Migración Flyway: Crear tabla de usuarios
-- Versión: 1
-- Descripción: Crea la tabla users con todos los campos necesarios

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índice único para email (ya está incluido en UNIQUE, pero lo dejamos explícito)
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_email ON users(email);

-- Comentarios en las columnas para documentación
COMMENT ON TABLE users IS 'Tabla que almacena la información de los usuarios del sistema';
COMMENT ON COLUMN users.id IS 'Identificador único del usuario';
COMMENT ON COLUMN users.name IS 'Nombre completo del usuario';
COMMENT ON COLUMN users.email IS 'Email único del usuario, usado para autenticación';
COMMENT ON COLUMN users.password_hash IS 'Hash de la contraseña del usuario (nunca almacenar en texto plano)';
COMMENT ON COLUMN users.role IS 'Rol del usuario: CUSTOMER, ADMIN, SELLER';
COMMENT ON COLUMN users.created_at IS 'Fecha y hora de creación del registro';

