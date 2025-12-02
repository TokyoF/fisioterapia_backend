package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.AuthResponse;
import com.fisioterapia.fisioterapia_backend.dto.LoginRequest;
import com.fisioterapia.fisioterapia_backend.dto.RegisterRequest;
import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import com.fisioterapia.fisioterapia_backend.entity.Role;
import com.fisioterapia.fisioterapia_backend.entity.User;
import com.fisioterapia.fisioterapia_backend.repository.PacienteRepository;
import com.fisioterapia.fisioterapia_backend.repository.RoleRepository;
import com.fisioterapia.fisioterapia_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PacienteRepository pacienteRepository;

    public AuthResponse login(LoginRequest request) {
        log.info("Intento de login para usuario/email: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtService.generateToken(authentication);

            // Buscar por username o email
            User user = userRepository.findByUsername(request.getUsername())
                    .or(() -> userRepository.findByEmail(request.getUsername()))
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            log.info("Login exitoso para usuario: {} ({})", user.getUsername(), user.getEmail());

            return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
            );
        } catch (Exception e) {
            log.error("Error en login para usuario/email: {} - {}", request.getUsername(), e.getMessage());
            throw e;
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, String roleName) {
        log.info("Intento de registro para usuario: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setIsActive(true);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("Usuario registrado exitosamente: {}", request.getUsername());

        // Si es un paciente, crear el registro de Paciente automáticamente
        if ("ROLE_PACIENTE".equals(roleName)) {
            crearRegistroPaciente(user);
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(authentication);

        return new AuthResponse(
            token,
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }

    private void crearRegistroPaciente(User user) {
        try {
            // Generar código único para el paciente
            String codigo = generarCodigoPaciente();
            
            Paciente paciente = new Paciente();
            paciente.setUser(user);
            paciente.setCodigo(codigo);
            paciente.setFechaNacimiento(LocalDate.now().minusYears(18)); // Fecha por defecto (18 años)
            paciente.setGenero("N/A"); // Valor corto para no exceder límite de columna
            paciente.setTelefono(user.getPhone());
            paciente.setActivo(true);
            
            pacienteRepository.save(paciente);
            log.info("Registro de paciente creado exitosamente para usuario: {} con código: {}", user.getUsername(), codigo);
        } catch (Exception e) {
            log.error("Error al crear registro de paciente para usuario: {}", user.getUsername(), e);
            throw new RuntimeException("Error al crear el perfil de paciente: " + e.getMessage());
        }
    }

    private String generarCodigoPaciente() {
        // Obtener el último código de paciente
        String ultimoCodigo = pacienteRepository.findTopByOrderByIdDesc()
                .map(Paciente::getCodigo)
                .orElse("PAT-000");
        
        // Extraer el número del código y incrementar
        int numero = Integer.parseInt(ultimoCodigo.substring(4)) + 1;
        
        return String.format("PAT-%03d", numero);
    }

    @Transactional
    public void sendPasswordResetEmail(String email) {
        log.info("Solicitud de restablecimiento de contraseña para email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No se encontró un usuario con ese email"));

        // Generar código de 6 dígitos
        String resetCode = generateResetCode();

        // Guardar el código en el usuario con expiración de 15 minutos
        user.setResetCode(resetCode);
        user.setResetCodeExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        log.info("Código de restablecimiento generado para: {} - Expira en 15 minutos", email);

        // Enviar email con el código
        try {
            emailService.enviarCodigoRecuperacionPassword(email, user.getFirstName(), resetCode);
            log.info("Email con código de recuperación enviado exitosamente a: {}", email);
        } catch (Exception e) {
            log.error("Error al enviar email de recuperación a: {} - {}", email, e.getMessage());
            throw new RuntimeException("Error al enviar el email de recuperación. Por favor intenta más tarde.");
        }
    }

    @Transactional
    public void verifyResetCode(String email, String code) {
        log.info("Verificación de código de restablecimiento para email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el código coincida
        if (user.getResetCode() == null || !user.getResetCode().equals(code)) {
            log.warn("Código incorrecto proporcionado para usuario: {}", email);
            throw new RuntimeException("Código de verificación incorrecto");
        }

        // Verificar que no haya expirado
        if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Intento de usar código expirado para usuario: {}", email);
            throw new RuntimeException("El código ha expirado. Por favor solicita uno nuevo.");
        }

        log.info("Código verificado exitosamente para usuario: {}", email);
    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        log.info("Intento de restablecimiento de contraseña para email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el código coincida
        if (user.getResetCode() == null || !user.getResetCode().equals(code)) {
            log.warn("Código incorrecto proporcionado para usuario: {}", email);
            throw new RuntimeException("Código de verificación incorrecto");
        }

        // Verificar si el código ha expirado
        if (user.getResetCodeExpiry() == null || user.getResetCodeExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Intento de usar código expirado para usuario: {}", email);
            throw new RuntimeException("El código ha expirado. Por favor solicita uno nuevo.");
        }

        // Validar que la nueva contraseña no esté vacía
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        if (newPassword.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        // Actualizar la contraseña y limpiar el código
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetCode(null);
        user.setResetCodeExpiry(null);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        log.info("Contraseña restablecida exitosamente para usuario: {}", email);
    }

    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateResetCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // Genera un número entre 100000 y 999999
        return String.valueOf(code);
    }
}
