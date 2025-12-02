# Scripts de Base de Datos

## Orden de Ejecución

### 1. Iniciar la aplicación primero
```bash
cd fisioterapia_backend
./mvnw spring-boot:run
```

La aplicación creará automáticamente las tablas necesarias con `spring.jpa.hibernate.ddl-auto=update`.

### 2. Ejecutar script de datos de prueba

**Opción A: Desde MySQL Workbench o cliente MySQL**
```bash
mysql -u luffy -p fisioterapia-backend < scripts/04_complete_test_data.sql
# Password: luffy
```

**Opción B: Desde Docker**
```bash
docker exec -i fisioterapia_backend-mysql-1 mysql -uluffy -pluffy fisioterapia-backend < scripts/04_complete_test_data.sql
```

**Opción C: Copiar al contenedor y ejecutar**
```bash
docker cp scripts/04_complete_test_data.sql fisioterapia_backend-mysql-1:/tmp/
docker exec -it fisioterapia_backend-mysql-1 bash
mysql -uluffy -pluffy fisioterapia-backend < /tmp/04_complete_test_data.sql
exit
```

## Credenciales de Prueba

Todos los usuarios tienen la contraseña: **password123**

### Administrador
- **Usuario:** `admin`
- **Email:** admin@fisioterapia.com
- **Rol:** ROLE_ADMIN

### Fisioterapeutas
| Usuario | Email | Especialidad |
|---------|-------|--------------|
| carlos.mendez | carlos.mendez@fisioterapia.com | Rehabilitación Deportiva |
| ana.torres | ana.torres@fisioterapia.com | Terapia Manual |
| luis.ramirez | luis.ramirez@fisioterapia.com | Neurorehabilitación |

### Pacientes
| Usuario | Email | Código |
|---------|-------|--------|
| juan.perez | juan.perez@email.com | PAT-001 |
| maria.lopez | maria.lopez@email.com | PAT-002 |
| pedro.sanchez | pedro.sanchez@email.com | PAT-003 |
| lucia.martinez | lucia.martinez@email.com | PAT-004 |
| roberto.diaz | roberto.diaz@email.com | PAT-005 |

## Datos Incluidos

- ✅ 3 Fisioterapeutas con diferentes especialidades
- ✅ 5 Pacientes con información completa
- ✅ 10+ Horarios disponibles distribuidos entre fisioterapeutas
- ✅ 8 Citas (4 programadas, 3 completadas, 1 cancelada)
- ✅ 4 Registros clínicos con diagnósticos y tratamientos

## Verificar Datos

```sql
-- Ver usuarios y roles
SELECT u.username, u.email, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id;

-- Ver fisioterapeutas
SELECT f.codigo, u.first_name, u.last_name, f.especialidad
FROM fisioterapeutas f
JOIN users u ON f.user_id = u.id;

-- Ver pacientes
SELECT p.codigo, u.first_name, u.last_name, p.fecha_nacimiento
FROM pacientes p
JOIN users u ON p.user_id = u.id;

-- Ver citas
SELECT c.codigo, c.fecha, c.hora, c.estado,
       CONCAT(up.first_name, ' ', up.last_name) as paciente,
       CONCAT(uf.first_name, ' ', uf.last_name) as fisioterapeuta
FROM citas c
JOIN pacientes p ON c.paciente_id = p.id
JOIN users up ON p.user_id = up.id
JOIN fisioterapeutas f ON c.fisioterapeuta_id = f.id
JOIN users uf ON f.user_id = uf.id;
```

## Limpiar Datos

Si necesitas empezar de nuevo:

```sql
-- CUIDADO: Esto borrará TODOS los datos
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE registros_clinicos;
TRUNCATE TABLE citas;
TRUNCATE TABLE horarios_disponibles;
TRUNCATE TABLE pacientes;
TRUNCATE TABLE fisioterapeutas;
TRUNCATE TABLE user_roles;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- Luego ejecuta el script 04_complete_test_data.sql nuevamente
```
