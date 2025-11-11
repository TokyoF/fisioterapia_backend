INSERT INTO `roles` (`name`, `description`) VALUES
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_FISIOTERAPEUTA', 'Fisioterapeuta profesional'),
('ROLE_CLIENTE', 'Cliente o paciente')
ON DUPLICATE KEY UPDATE `description` = VALUES(`description`);
