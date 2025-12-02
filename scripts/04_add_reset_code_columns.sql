-- Script para agregar columnas de código de recuperación a la tabla users
-- Ejecutar este script si ya tienes la base de datos creada

USE `fisioterapia-backend`;

-- Agregar columnas para el código de recuperación
ALTER TABLE users 
ADD COLUMN reset_code VARCHAR(6) NULL AFTER reset_token_expiry,
ADD COLUMN reset_code_expiry DATETIME NULL AFTER reset_code;

SELECT 'Columnas de código de recuperación agregadas exitosamente' AS mensaje;
