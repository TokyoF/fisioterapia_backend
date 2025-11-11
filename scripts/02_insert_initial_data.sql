INSERT INTO `roles` (`name`, `description`) VALUES
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_FISIOTERAPEUTA', 'Fisioterapeuta profesional'),
('ROLE_CLIENTE', 'Cliente o paciente')
ON DUPLICATE KEY UPDATE `description` = VALUES(`description`);

INSERT INTO `users` (`username`, `email`, `password`, `first_name`, `last_name`, `phone`, `is_active`) VALUES
('admin', 'admin@fisioterapia.com', '$2a$12$h9DQttBTwxCY8k1Y9vw94.YrZ92TIzxfyrZJ7m/D8IVTxcMruW1Li', 'Admin', 'Sistema', '999999999', TRUE)
ON DUPLICATE KEY UPDATE `email` = VALUES(`email`);

INSERT INTO `user_roles` (`user_id`, `role_id`)
SELECT u.id, r.id
FROM `users` u, `roles` r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE `user_id` = VALUES(`user_id`);
