-- Script para actualizar el tamaño de la columna genero en la tabla pacientes
-- Ejecutar este script si ya tienes la base de datos creada

USE `fisioterapia-backend`;

-- Modificar la columna genero para que acepte hasta 20 caracteres
ALTER TABLE pacientes MODIFY COLUMN genero VARCHAR(20);

-- Actualizar registros existentes que puedan tener problemas
UPDATE pacientes 
SET genero = 'N/A' 
WHERE genero IS NULL OR genero = '';

SELECT 'Columna genero actualizada exitosamente' AS mensaje;
