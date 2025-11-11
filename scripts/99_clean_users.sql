-- Script para limpiar usuarios y empezar de cero
-- Ejecuta esto SOLO si tuviste problemas con los scripts anteriores

DELETE FROM user_roles;
DELETE FROM users;

-- Opcional: también limpiar roles si es necesario
-- DELETE FROM roles;
