package com.fisioterapia.fisioterapia_backend.config;

import com.fisioterapia.fisioterapia_backend.entity.Role;
import com.fisioterapia.fisioterapia_backend.entity.User;
import com.fisioterapia.fisioterapia_backend.repository.RoleRepository;
import com.fisioterapia.fisioterapia_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando carga de datos iniciales...");

        createRolesIfNotExist();
        createDefaultUsers();

        log.info("Carga de datos iniciales completada.");
    }

    private void createRolesIfNotExist() {
        createRoleIfNotExist("ROLE_ADMIN", "Administrador del sistema");
        createRoleIfNotExist("ROLE_FISIOTERAPEUTA", "Fisioterapeuta profesional");
        createRoleIfNotExist("ROLE_CLIENTE", "Cliente o paciente");
    }

    private void createRoleIfNotExist(String name, String description) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            roleRepository.save(role);
            log.info("Rol creado: {}", name);
        } else {
            log.info("Rol ya existe: {}", name);
        }
    }

    private void createDefaultUsers() {
        createUserIfNotExist("admin", "admin@fisioterapia.com", "admin123",
                "Admin", "Sistema", "999999999", "ROLE_ADMIN");

        createUserIfNotExist("fisio", "fisio@fisioterapia.com", "fisio123",
                "Carlos", "Mendoza", "988888888", "ROLE_FISIOTERAPEUTA");

        createUserIfNotExist("cliente", "cliente@fisioterapia.com", "cliente123",
                "Maria", "Rodriguez", "977777777", "ROLE_CLIENTE");
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
            log.info("Usuario {} creado - Username: {}, Password: {}", roleName, username, password);
        } else {
            log.info("Usuario {} ya existe", username);
        }
    }
}
