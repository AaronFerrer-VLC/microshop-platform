-- Script SQL para limpiar el historial de Flyway
-- Ejecuta este script en PostgreSQL si prefieres limpiar completamente el historial
-- y empezar de nuevo con las migraciones

-- Conecta a la base de datos microshopdb
-- \c microshopdb

-- Eliminar el historial de Flyway para product-service
-- (ajusta el schema si usas schemas diferentes)
DELETE FROM flyway_schema_history WHERE script LIKE '%V1__Create_products_table%';

-- Eliminar el historial de Flyway para user-service
DELETE FROM flyway_schema_history WHERE script LIKE '%V1__Create_users_table%';

-- O si quieres eliminar TODO el historial de Flyway:
-- TRUNCATE TABLE flyway_schema_history;

-- Después de ejecutar esto, reinicia los servicios y Flyway volverá a aplicar las migraciones

