-- Datos de prueba para testing de la aplicación
-- Ejecutar después de que la aplicación cree las tablas automáticamente

-- Insertar usuarios de fisioterapeutas
INSERT INTO users (username, email, password, first_name, last_name, phone, is_active) VALUES
('carlos.mendez', 'carlos.mendez@fisioterapia.com', '$2a$10$XYZ...', 'Carlos', 'Méndez', '+1234567890', true),
('ana.torres', 'ana.torres@fisioterapia.com', '$2a$10$XYZ...', 'Ana', 'Torres', '+1234567891', true),
('luis.ramirez', 'luis.ramirez@fisioterapia.com', '$2a$10$XYZ...', 'Luis', 'Ramírez', '+1234567892', true);

-- Asignar rol FISIOTERAPEUTA (asumiendo que los IDs de usuarios son 2, 3, 4 y rol FISIOTERAPEUTA tiene ID 2)
INSERT INTO user_roles (user_id, role_id) VALUES
(2, 2),
(3, 2),
(4, 2);

-- Insertar fisioterapeutas
INSERT INTO fisioterapeutas (codigo, especialidad, licencia_profesional, anios_experiencia, user_id) VALUES
('FIS-001', 'Rehabilitación Deportiva', 'LIC-12345', 10, 2),
('FIS-002', 'Terapia Manual', 'LIC-12346', 8, 3),
('FIS-003', 'Neurorehabilitación', 'LIC-12347', 12, 4);

-- Insertar usuario paciente de prueba
INSERT INTO users (username, email, password, first_name, last_name, phone, is_active) VALUES
('juan.perez', 'juan.perez@email.com', '$2a$10$XYZ...', 'Juan', 'Pérez', '+1234567893', true);

-- Asignar rol PACIENTE (asumiendo que el ID de usuario es 5 y rol PACIENTE tiene ID 4)
INSERT INTO user_roles (user_id, role_id) VALUES
(5, 4);

-- Insertar paciente
INSERT INTO pacientes (codigo, fecha_nacimiento, genero, tipo_sangre, alergias, antecedentes, user_id) VALUES
('PAT-001', '1990-05-15', 'M', 'O+', 'Penicilina', 'Cirugía de menisco 2023', 5);

-- Nota: Las contraseñas deberán ser hasheadas con BCrypt
-- Contraseña de ejemplo: "password123" hasheada sería algo como:
-- $2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S
