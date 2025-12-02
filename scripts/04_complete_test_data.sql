-- Script completo de datos de prueba para la aplicación
-- IMPORTANTE: Ejecutar después de que la aplicación haya creado las tablas
-- Contraseña para todos los usuarios: "password123"

-- ========================================
-- 1. ROLES (ya deberían existir)
-- ========================================
INSERT INTO roles (name, description) VALUES
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_FISIOTERAPEUTA', 'Fisioterapeuta profesional'),
('ROLE_ASISTENTE', 'Asistente administrativo'),
('ROLE_PACIENTE', 'Paciente del centro')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- ========================================
-- 2. USUARIOS
-- ========================================

-- Administrador
INSERT INTO users (username, email, password, first_name, last_name, phone, is_active) VALUES
('admin', 'admin@fisioterapia.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'María', 'González', '+593991234567', true)
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Fisioterapeutas
INSERT INTO users (username, email, password, first_name, last_name, phone, is_active) VALUES
('carlos.mendez', 'carlos.mendez@fisioterapia.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Carlos', 'Méndez', '+593991234568', true),
('ana.torres', 'ana.torres@fisioterapia.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Ana', 'Torres', '+593991234569', true),
('luis.ramirez', 'luis.ramirez@fisioterapia.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Luis', 'Ramírez', '+593991234570', true)
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- Pacientes
INSERT INTO users (username, email, password, first_name, last_name, phone, is_active) VALUES
('juan.perez', 'juan.perez@email.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Juan', 'Pérez', '+593991234571', true),
('maria.lopez', 'maria.lopez@email.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'María', 'López', '+593991234572', true),
('pedro.sanchez', 'pedro.sanchez@email.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Pedro', 'Sánchez', '+593991234573', true),
('lucia.martinez', 'lucia.martinez@email.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Lucía', 'Martínez', '+593991234574', true),
('roberto.diaz', 'roberto.diaz@email.com', '$2a$10$rT7h8V4bqxfzLJKQH5gJKOxNm5YhZ8LqYJ3vP2xR4tKzWnH7gS8.S', 'Roberto', 'Díaz', '+593991234575', true)
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- ========================================
-- 3. ASIGNAR ROLES A USUARIOS
-- ========================================

-- Admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Fisioterapeutas
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'carlos.mendez' AND r.name = 'ROLE_FISIOTERAPEUTA'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'ana.torres' AND r.name = 'ROLE_FISIOTERAPEUTA'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'luis.ramirez' AND r.name = 'ROLE_FISIOTERAPEUTA'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- Pacientes
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username IN ('juan.perez', 'maria.lopez', 'pedro.sanchez', 'lucia.martinez', 'roberto.diaz')
AND r.name = 'ROLE_PACIENTE'
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- ========================================
-- 4. FISIOTERAPEUTAS
-- ========================================

INSERT INTO fisioterapeutas (codigo, especialidad, licencia_profesional, anios_experiencia, user_id)
SELECT 'FIS-001', 'Rehabilitación Deportiva', 'LIC-12345', 10, u.id
FROM users u WHERE u.username = 'carlos.mendez'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

INSERT INTO fisioterapeutas (codigo, especialidad, licencia_profesional, anios_experiencia, user_id)
SELECT 'FIS-002', 'Terapia Manual', 'LIC-12346', 8, u.id
FROM users u WHERE u.username = 'ana.torres'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

INSERT INTO fisioterapeutas (codigo, especialidad, licencia_profesional, anios_experiencia, user_id)
SELECT 'FIS-003', 'Neurorehabilitación', 'LIC-12347', 12, u.id
FROM users u WHERE u.username = 'luis.ramirez'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

-- ========================================
-- 5. PACIENTES
-- ========================================

INSERT INTO pacientes (codigo, fecha_nacimiento, genero, tipo_sangre, alergias, antecedentes, user_id)
SELECT 'PAT-001', '1990-05-15', 'M', 'O+', 'Penicilina', 'Cirugía de menisco 2023', u.id
FROM users u WHERE u.username = 'juan.perez'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

INSERT INTO pacientes (codigo, fecha_nacimiento, genero, tipo_sangre, alergias, antecedentes, user_id)
SELECT 'PAT-002', '1985-08-22', 'F', 'A+', 'Ninguna', 'Esguince de tobillo recurrente', u.id
FROM users u WHERE u.username = 'maria.lopez'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

INSERT INTO pacientes (codigo, fecha_nacimiento, genero, tipo_sangre, alergias, antecedentes, user_id)
SELECT 'PAT-003', '1978-12-10', 'M', 'B+', 'Ibuprofeno', 'Hernia discal L4-L5', u.id
FROM users u WHERE u.username = 'pedro.sanchez'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

INSERT INTO pacientes (codigo, fecha_nacimiento, genero, tipo_sangre, alergias, antecedentes, user_id)
SELECT 'PAT-004', '1995-03-18', 'F', 'O-', 'Polen', 'Fractura de muñeca 2022', u.id
FROM users u WHERE u.username = 'lucia.martinez'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

INSERT INTO pacientes (codigo, fecha_nacimiento, genero, tipo_sangre, alergias, antecedentes, user_id)
SELECT 'PAT-005', '1988-07-25', 'M', 'AB+', 'Ninguna', 'Tendinitis crónica', u.id
FROM users u WHERE u.username = 'roberto.diaz'
ON DUPLICATE KEY UPDATE codigo = VALUES(codigo);

-- ========================================
-- 6. HORARIOS DE FISIOTERAPEUTAS
-- ========================================

-- Horarios para Carlos Méndez (FIS-001)
INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'LUNES', '08:00:00', '09:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-001';

INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'LUNES', '09:00:00', '10:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-001';

INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'MARTES', '14:00:00', '15:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-001';

INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'MIERCOLES', '10:00:00', '11:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-001';

-- Horarios para Ana Torres (FIS-002)
INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'MARTES', '08:00:00', '09:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-002';

INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'MIERCOLES', '14:00:00', '15:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-002';

INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'JUEVES', '09:00:00', '10:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-002';

-- Horarios para Luis Ramírez (FIS-003)
INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'LUNES', '15:00:00', '16:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-003';

INSERT INTO horarios_disponibles (dia_semana, hora_inicio, hora_fin, disponible, fisioterapeuta_id)
SELECT 'VIERNES', '08:00:00', '09:00:00', true, f.id
FROM fisioterapeutas f WHERE f.codigo = 'FIS-003';

-- ========================================
-- 7. CITAS
-- ========================================

-- Citas programadas
INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-001', DATE_ADD(CURDATE(), INTERVAL 2 DAY), '09:00:00', 'PROGRAMADA', 'Rehabilitación de rodilla post-cirugía', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-001' AND f.codigo = 'FIS-001';

INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-002', DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', 'CONFIRMADA', 'Dolor lumbar crónico', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-002' AND f.codigo = 'FIS-002';

INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-003', DATE_ADD(CURDATE(), INTERVAL 5 DAY), '10:00:00', 'PROGRAMADA', 'Seguimiento hernia discal', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-003' AND f.codigo = 'FIS-001';

INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-004', DATE_ADD(CURDATE(), INTERVAL 7 DAY), '09:00:00', 'PROGRAMADA', 'Terapia de muñeca', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-004' AND f.codigo = 'FIS-002';

-- Citas completadas (pasadas)
INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-005', DATE_SUB(CURDATE(), INTERVAL 5 DAY), '08:00:00', 'COMPLETADA', 'Primera sesión rehabilitación', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-001' AND f.codigo = 'FIS-001';

INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-006', DATE_SUB(CURDATE(), INTERVAL 10 DAY), '14:00:00', 'COMPLETADA', 'Evaluación inicial', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-002' AND f.codigo = 'FIS-002';

INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-007', DATE_SUB(CURDATE(), INTERVAL 3 DAY), '15:00:00', 'COMPLETADA', 'Terapia de neurorehabilitación', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-005' AND f.codigo = 'FIS-003';

-- Cita cancelada
INSERT INTO citas (codigo, fecha, hora, estado, motivo, paciente_id, fisioterapeuta_id)
SELECT 'CIT-008', DATE_SUB(CURDATE(), INTERVAL 2 DAY), '10:00:00', 'CANCELADA', 'Cancelada por paciente', p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-003' AND f.codigo = 'FIS-001';

-- ========================================
-- 8. REGISTROS CLÍNICOS
-- ========================================

-- Registros para Juan Pérez (PAT-001)
INSERT INTO registros_clinicos (fecha, diagnostico, tratamiento, observaciones, paciente_id, fisioterapeuta_id)
SELECT DATE_SUB(CURDATE(), INTERVAL 5 DAY),
       'Esguince de rodilla grado II - Ligamento colateral medial',
       'Aplicación de electroterapia TENS, crioterapia y ejercicios isométricos de cuádriceps. Vendaje funcional.',
       'Paciente presenta inflamación moderada. Rango de movimiento limitado a 90 grados. Dolor 6/10 en escala EVA.',
       p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-001' AND f.codigo = 'FIS-001';

-- Registros para María López (PAT-002)
INSERT INTO registros_clinicos (fecha, diagnostico, tratamiento, observaciones, paciente_id, fisioterapeuta_id)
SELECT DATE_SUB(CURDATE(), INTERVAL 10 DAY),
       'Contractura muscular paravertebral lumbar',
       'Terapia manual (masaje descontracturante), estiramientos miofasciales. Educación postural.',
       'Tensión muscular bilateral. Dolor 7/10. Se recomienda continuar con ejercicios en casa.',
       p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-002' AND f.codigo = 'FIS-002';

INSERT INTO registros_clinicos (fecha, diagnostico, tratamiento, observaciones, paciente_id, fisioterapeuta_id)
SELECT DATE_SUB(CURDATE(), INTERVAL 8 DAY),
       'Seguimiento contractura lumbar - Evolución favorable',
       'Continuación terapia manual, ejercicios de core stability. Técnicas de relajación.',
       'Reducción de dolor a 4/10. Mejora en movilidad. Paciente reporta mejor calidad de sueño.',
       p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-002' AND f.codigo = 'FIS-002';

-- Registros para Roberto Díaz (PAT-005)
INSERT INTO registros_clinicos (fecha, diagnostico, tratamiento, observaciones, paciente_id, fisioterapeuta_id)
SELECT DATE_SUB(CURDATE(), INTERVAL 3 DAY),
       'Tendinitis crónica del supraespinoso',
       'Terapia de ejercicios progresivos, fortalecimiento del manguito rotador. Movilización articular.',
       'Paciente muestra buena adherencia al tratamiento. Dolor reducido a 3/10. Rango de abducción mejorado.',
       p.id, f.id
FROM pacientes p, fisioterapeutas f
WHERE p.codigo = 'PAT-005' AND f.codigo = 'FIS-003';

-- ========================================
-- RESUMEN DE CREDENCIALES
-- ========================================
-- Usuario: admin / Password: password123 (ROLE_ADMIN)
-- Usuario: carlos.mendez / Password: password123 (ROLE_FISIOTERAPEUTA)
-- Usuario: ana.torres / Password: password123 (ROLE_FISIOTERAPEUTA)
-- Usuario: luis.ramirez / Password: password123 (ROLE_FISIOTERAPEUTA)
-- Usuario: juan.perez / Password: password123 (ROLE_PACIENTE)
-- Usuario: maria.lopez / Password: password123 (ROLE_PACIENTE)
-- Usuario: pedro.sanchez / Password: password123 (ROLE_PACIENTE)
-- Usuario: lucia.martinez / Password: password123 (ROLE_PACIENTE)
-- Usuario: roberto.diaz / Password: password123 (ROLE_PACIENTE)
