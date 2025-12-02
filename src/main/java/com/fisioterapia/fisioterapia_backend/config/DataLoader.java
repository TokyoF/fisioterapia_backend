package com.fisioterapia.fisioterapia_backend.config;

import com.fisioterapia.fisioterapia_backend.entity.*;
import com.fisioterapia.fisioterapia_backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final PacienteRepository pacienteRepository;
    private final HorarioRepository horarioRepository;
    private final CitaRepository citaRepository;
    private final RegistroClinicoRepository registroClinicoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("========================================");
        log.info("Iniciando carga de datos iniciales...");
        log.info("========================================");

        createRolesIfNotExist();
        createUsers();
        createFisioterapeutas();
        createPacientes();
        createHorarios();
        createCitas();
        createRegistrosClinicos();

        log.info("========================================");
        log.info("Carga de datos iniciales completada.");
        log.info("========================================");
        log.info("CREDENCIALES DE PRUEBA:");
        log.info("Admin: admin@fisioterapia.com / password123");
        log.info("Fisioterapeutas:");
        log.info("  - carlos.mendez@fisioterapia.com / password123");
        log.info("  - ana.torres@fisioterapia.com / password123");
        log.info("  - luis.ramirez@fisioterapia.com / password123");
        log.info("Pacientes:");
        log.info("  - juan.perez@email.com / password123");
        log.info("  - maria.lopez@email.com / password123");
        log.info("  - pedro.sanchez@email.com / password123");
        log.info("  - lucia.martinez@email.com / password123");
        log.info("  - roberto.diaz@email.com / password123");
        log.info("========================================");
    }

    private void createRolesIfNotExist() {
        log.info("Creando roles...");
        createRoleIfNotExist("ROLE_ADMIN", "Administrador del sistema");
        createRoleIfNotExist("ROLE_FISIOTERAPEUTA", "Fisioterapeuta profesional");
        createRoleIfNotExist("ROLE_ASISTENTE", "Asistente administrativo");
        createRoleIfNotExist("ROLE_PACIENTE", "Paciente del centro");
    }

    private void createRoleIfNotExist(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
            log.info("✓ Rol creado: {}", name);
        } else {
            log.info("✓ Rol ya existe: {}", name);
        }
    }

    private void createUsers() {
        log.info("Creando usuarios...");

        // Administrador - usando email como username
        createUserIfNotExist("admin@fisioterapia.com", "admin@fisioterapia.com", "password123",
                "María", "González", "+593991234567", "ROLE_ADMIN");

        // Fisioterapeutas - usando email como username
        createUserIfNotExist("carlos.mendez@fisioterapia.com", "carlos.mendez@fisioterapia.com", "password123",
                "Carlos", "Méndez", "+593991234568", "ROLE_FISIOTERAPEUTA");
        createUserIfNotExist("ana.torres@fisioterapia.com", "ana.torres@fisioterapia.com", "password123",
                "Ana", "Torres", "+593991234569", "ROLE_FISIOTERAPEUTA");
        createUserIfNotExist("luis.ramirez@fisioterapia.com", "luis.ramirez@fisioterapia.com", "password123",
                "Luis", "Ramírez", "+593991234570", "ROLE_FISIOTERAPEUTA");

        // Pacientes - usando email como username
        createUserIfNotExist("juan.perez@email.com", "juan.perez@email.com", "password123",
                "Juan", "Pérez", "+593991234571", "ROLE_PACIENTE");
        createUserIfNotExist("maria.lopez@email.com", "maria.lopez@email.com", "password123",
                "María", "López", "+593991234572", "ROLE_PACIENTE");
        createUserIfNotExist("pedro.sanchez@email.com", "pedro.sanchez@email.com", "password123",
                "Pedro", "Sánchez", "+593991234573", "ROLE_PACIENTE");
        createUserIfNotExist("lucia.martinez@email.com", "lucia.martinez@email.com", "password123",
                "Lucía", "Martínez", "+593991234574", "ROLE_PACIENTE");
        createUserIfNotExist("roberto.diaz@email.com", "roberto.diaz@email.com", "password123",
                "Roberto", "Díaz", "+593991234575", "ROLE_PACIENTE");
    }

    private void createUserIfNotExist(String username, String email, String password,
                                      String firstName, String lastName, String phone, String roleName) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhone(phone);
            user.setIsActive(true);

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol " + roleName + " no encontrado"));

            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            userRepository.save(user);
            log.info("✓ Usuario {} creado: {}", roleName, username);
        } else {
            log.info("✓ Usuario ya existe: {}", username);
        }
    }

    private void createFisioterapeutas() {
        log.info("Creando fisioterapeutas...");

        createFisioterapeutaIfNotExist("carlos.mendez@fisioterapia.com",
                "Rehabilitación Deportiva", "LIC-12345");
        createFisioterapeutaIfNotExist("ana.torres@fisioterapia.com",
                "Terapia Manual", "LIC-12346");
        createFisioterapeutaIfNotExist("luis.ramirez@fisioterapia.com",
                "Neurorehabilitación", "LIC-12347");
    }

    private void createFisioterapeutaIfNotExist(String username,
                                                 String especialidad, String licencia) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario " + username + " no encontrado"));

        if (fisioterapeutaRepository.findByUserId(user.getId()).isEmpty()) {
            Fisioterapeuta fisio = new Fisioterapeuta();
            fisio.setEspecialidad(especialidad);
            fisio.setNumeroLicencia(licencia);
            fisio.setUser(user);
            fisioterapeutaRepository.save(fisio);
            log.info("✓ Fisioterapeuta creado: {} - {}", username, especialidad);
        } else {
            log.info("✓ Fisioterapeuta ya existe: {}", username);
        }
    }

    private void createPacientes() {
        log.info("Creando pacientes...");

        createPacienteIfNotExist("juan.perez@email.com", "PAT-001",
                LocalDate.of(1990, 5, 15), "M", "O+",
                "Penicilina", "Cirugía de menisco 2023");
        createPacienteIfNotExist("maria.lopez@email.com", "PAT-002",
                LocalDate.of(1985, 8, 22), "F", "A+",
                null, "Esguince de tobillo recurrente");
        createPacienteIfNotExist("pedro.sanchez@email.com", "PAT-003",
                LocalDate.of(1978, 12, 10), "M", "B+",
                "Ibuprofeno", "Hernia discal L4-L5");
        createPacienteIfNotExist("lucia.martinez@email.com", "PAT-004",
                LocalDate.of(1995, 3, 18), "F", "O-",
                "Polen", "Fractura de muñeca 2022");
        createPacienteIfNotExist("roberto.diaz@email.com", "PAT-005",
                LocalDate.of(1988, 7, 25), "M", "AB+",
                null, "Tendinitis crónica");
    }

    private void createPacienteIfNotExist(String username, String codigo,
                                          LocalDate fechaNacimiento, String genero,
                                          String tipoSangre, String alergias, String antecedentes) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario " + username + " no encontrado"));

        if (pacienteRepository.findByUserId(user.getId()).isEmpty()) {
            Paciente paciente = new Paciente();
            paciente.setCodigo(codigo);
            paciente.setFechaNacimiento(fechaNacimiento);
            paciente.setGenero(genero);
            paciente.setTipoSangre(tipoSangre);
            paciente.setAlergias(alergias);
            paciente.setAntecedentes(antecedentes);
            paciente.setUser(user);
            pacienteRepository.save(paciente);
            log.info("✓ Paciente creado: {} - {}", codigo, user.getFirstName());
        } else {
            log.info("✓ Paciente ya existe: {}", username);
        }
    }

    private void createHorarios() {
        log.info("Creando horarios...");

        // Horarios para Carlos Méndez (FIS-001)
        Fisioterapeuta carlos = getFisioByUsername("carlos.mendez@fisioterapia.com");
        createHorarioIfNotExist(carlos, "LUNES", LocalTime.of(8, 0), LocalTime.of(9, 0));
        createHorarioIfNotExist(carlos, "LUNES", LocalTime.of(9, 0), LocalTime.of(10, 0));
        createHorarioIfNotExist(carlos, "MARTES", LocalTime.of(14, 0), LocalTime.of(15, 0));
        createHorarioIfNotExist(carlos, "MIERCOLES", LocalTime.of(10, 0), LocalTime.of(11, 0));

        // Horarios para Ana Torres (FIS-002)
        Fisioterapeuta ana = getFisioByUsername("ana.torres@fisioterapia.com");
        createHorarioIfNotExist(ana, "MARTES", LocalTime.of(8, 0), LocalTime.of(9, 0));
        createHorarioIfNotExist(ana, "MIERCOLES", LocalTime.of(14, 0), LocalTime.of(15, 0));
        createHorarioIfNotExist(ana, "JUEVES", LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Horarios para Luis Ramírez (FIS-003)
        Fisioterapeuta luis = getFisioByUsername("luis.ramirez@fisioterapia.com");
        createHorarioIfNotExist(luis, "LUNES", LocalTime.of(15, 0), LocalTime.of(16, 0));
        createHorarioIfNotExist(luis, "VIERNES", LocalTime.of(8, 0), LocalTime.of(9, 0));
    }

    private void createHorarioIfNotExist(Fisioterapeuta fisio, String dia, LocalTime inicio, LocalTime fin) {
        if (horarioRepository.findByFisioterapeutaIdAndDiaSemana(fisio.getId(), dia).stream()
                .noneMatch(h -> h.getHoraInicio().equals(inicio) && h.getHoraFin().equals(fin))) {
            Horario horario = new Horario();
            horario.setDiaSemana(dia);
            horario.setHoraInicio(inicio);
            horario.setHoraFin(fin);
            horario.setDisponible(true);
            horario.setFisioterapeuta(fisio);
            horarioRepository.save(horario);
            log.info("✓ Horario creado: Fisioterapeuta ID {} - {} - {}", fisio.getId(), dia, inicio);
        }
    }

    private void createCitas() {
        log.info("Creando citas...");

        Fisioterapeuta carlos = getFisioByUsername("carlos.mendez@fisioterapia.com");
        Fisioterapeuta ana = getFisioByUsername("ana.torres@fisioterapia.com");
        Fisioterapeuta luis = getFisioByUsername("luis.ramirez@fisioterapia.com");

        Paciente juan = getPacienteByUsername("juan.perez@email.com");
        Paciente maria = getPacienteByUsername("maria.lopez@email.com");
        Paciente pedro = getPacienteByUsername("pedro.sanchez@email.com");
        Paciente lucia = getPacienteByUsername("lucia.martinez@email.com");
        Paciente roberto = getPacienteByUsername("roberto.diaz@email.com");

        // Citas programadas (futuras)
        createCitaIfNotExist("CIT-001", LocalDate.now().plusDays(2), LocalTime.of(9, 0),
                Cita.EstadoCita.PROGRAMADA, "Rehabilitación de rodilla post-cirugía", juan, carlos);
        createCitaIfNotExist("CIT-002", LocalDate.now().plusDays(3), LocalTime.of(14, 0),
                Cita.EstadoCita.CONFIRMADA, "Dolor lumbar crónico", maria, ana);
        createCitaIfNotExist("CIT-003", LocalDate.now().plusDays(5), LocalTime.of(10, 0),
                Cita.EstadoCita.PROGRAMADA, "Seguimiento hernia discal", pedro, carlos);
        createCitaIfNotExist("CIT-004", LocalDate.now().plusDays(7), LocalTime.of(9, 0),
                Cita.EstadoCita.PROGRAMADA, "Terapia de muñeca", lucia, ana);

        // Citas completadas (pasadas)
        createCitaIfNotExist("CIT-005", LocalDate.now().minusDays(5), LocalTime.of(8, 0),
                Cita.EstadoCita.COMPLETADA, "Primera sesión rehabilitación", juan, carlos);
        createCitaIfNotExist("CIT-006", LocalDate.now().minusDays(10), LocalTime.of(14, 0),
                Cita.EstadoCita.COMPLETADA, "Evaluación inicial", maria, ana);
        createCitaIfNotExist("CIT-007", LocalDate.now().minusDays(3), LocalTime.of(15, 0),
                Cita.EstadoCita.COMPLETADA, "Terapia de neurorehabilitación", roberto, luis);

        // Cita cancelada
        createCitaIfNotExist("CIT-008", LocalDate.now().minusDays(2), LocalTime.of(10, 0),
                Cita.EstadoCita.CANCELADA, "Cancelada por paciente", pedro, carlos);
    }

    private void createCitaIfNotExist(String codigo, LocalDate fecha, LocalTime hora,
                                      Cita.EstadoCita estado, String motivo,
                                      Paciente paciente, Fisioterapeuta fisio) {
        if (citaRepository.findByCodigo(codigo).isEmpty()) {
            Cita cita = new Cita();
            cita.setCodigo(codigo);
            cita.setFecha(fecha);
            cita.setHora(hora);
            cita.setEstado(estado);
            cita.setMotivo(motivo);
            cita.setPaciente(paciente);
            cita.setFisioterapeuta(fisio);
            citaRepository.save(cita);
            log.info("✓ Cita creada: {} - {} - {}", codigo, fecha, estado);
        }
    }

    private void createRegistrosClinicos() {
        log.info("Creando registros clínicos...");

        Fisioterapeuta carlos = getFisioByUsername("carlos.mendez@fisioterapia.com");
        Fisioterapeuta ana = getFisioByUsername("ana.torres@fisioterapia.com");
        Fisioterapeuta luis = getFisioByUsername("luis.ramirez@fisioterapia.com");

        Paciente juan = getPacienteByUsername("juan.perez@email.com");
        Paciente maria = getPacienteByUsername("maria.lopez@email.com");
        Paciente roberto = getPacienteByUsername("roberto.diaz@email.com");

        // Registro para Juan Pérez
        createRegistroIfNotExist("REC-001", LocalDate.now().minusDays(5), juan, carlos,
                "Esguince de rodilla grado II - Ligamento colateral medial",
                "Aplicación de electroterapia TENS, crioterapia y ejercicios isométricos de cuádriceps. Vendaje funcional.",
                "Paciente presenta inflamación moderada. Rango de movimiento limitado a 90 grados. Dolor 6/10 en escala EVA.");

        // Registros para María López
        createRegistroIfNotExist("REC-002", LocalDate.now().minusDays(10), maria, ana,
                "Contractura muscular paravertebral lumbar",
                "Terapia manual (masaje descontracturante), estiramientos miofasciales. Educación postural.",
                "Tensión muscular bilateral. Dolor 7/10. Se recomienda continuar con ejercicios en casa.");

        createRegistroIfNotExist("REC-003", LocalDate.now().minusDays(8), maria, ana,
                "Seguimiento contractura lumbar - Evolución favorable",
                "Continuación terapia manual, ejercicios de core stability. Técnicas de relajación.",
                "Reducción de dolor a 4/10. Mejora en movilidad. Paciente reporta mejor calidad de sueño.");

        // Registro para Roberto Díaz
        createRegistroIfNotExist("REC-004", LocalDate.now().minusDays(3), roberto, luis,
                "Tendinitis crónica del supraespinoso",
                "Terapia de ejercicios progresivos, fortalecimiento del manguito rotador. Movilización articular.",
                "Paciente muestra buena adherencia al tratamiento. Dolor reducido a 3/10. Rango de abducción mejorado.");
    }

    private void createRegistroIfNotExist(String codigo, LocalDate fecha, Paciente paciente,
                                          Fisioterapeuta fisio, String diagnostico,
                                          String tratamiento, String observaciones) {
        if (registroClinicoRepository.findByCodigo(codigo).isEmpty()) {
            RegistroClinico registro = new RegistroClinico();
            registro.setCodigo(codigo);
            registro.setFechaRegistro(fecha);
            registro.setDiagnostico(diagnostico);
            registro.setTratamiento(tratamiento);
            registro.setObservaciones(observaciones);
            registro.setPaciente(paciente);
            registro.setFisioterapeuta(fisio);
            registroClinicoRepository.save(registro);
            log.info("✓ Registro clínico creado: {} - {}", codigo, fecha);
        }
    }

    private Fisioterapeuta getFisioByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario " + username + " no encontrado"));
        return fisioterapeutaRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta " + username + " no encontrado"));
    }

    private Paciente getPacienteByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario " + username + " no encontrado"));
        return pacienteRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Paciente " + username + " no encontrado"));
    }
}
