
package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.PacienteResponse;
import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import com.fisioterapia.fisioterapia_backend.entity.User;
import com.fisioterapia.fisioterapia_backend.repository.PacienteRepository;
import com.fisioterapia.fisioterapia_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {

  private final PacienteRepository pacienteRepository;
  private final UserRepository userRepository;

  // 1) Paciente del usuario autenticado
  @Transactional(readOnly = true)
  public Optional<PacienteResponse> obtenerPacienteActual(Authentication authentication) {

    // Si tu entidad User implementa UserDetails, podrías hacer:
    // User user = (User) authentication.getPrincipal();
    // Long userId = user.getId();

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    Long userId = userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
        .getId();

    return pacienteRepository.findByUserId(userId)
        .map(this::convertirAResponse);
  }

  // 2) Paciente por ID (para ver cualquier paciente específico)
  @Transactional(readOnly = true)
  public Optional<PacienteResponse> obtenerPorId(Long pacienteId) {
    return pacienteRepository.findById(pacienteId)
        .map(this::convertirAResponse);
  }

  // 3) Listar todos los pacientes
  @Transactional(readOnly = true)
  public List<PacienteResponse> listarTodos() {
    return pacienteRepository.findAll()
        .stream()
        .map(this::convertirAResponse)
        .collect(Collectors.toList());
  }

  // --------- Mapper ---------
  private PacienteResponse convertirAResponse(Paciente paciente) {
    int edad = 0;
    if (paciente.getFechaNacimiento() != null) {
      edad = Period.between(paciente.getFechaNacimiento(), LocalDate.now()).getYears();
    }

    return PacienteResponse.builder()
        .id(paciente.getId())
        .codigo(paciente.getCodigo())
        .nombre(paciente.getUser().getFirstName() + " " + paciente.getUser().getLastName())
        .edad(edad)
        .genero(paciente.getGenero())
        .telefono(paciente.getTelefono())
        .email(paciente.getUser().getEmail())
        .tipoSangre(paciente.getTipoSangre())
        .alergias(paciente.getAlergias())
        .antecedentes(paciente.getAntecedentes())
        .fechaNacimiento(paciente.getFechaNacimiento())
        .user(PacienteResponse.UserInfo.builder()
            .id(paciente.getUser().getId())
            .username(paciente.getUser().getUsername())
            .firstName(paciente.getUser().getFirstName())
            .lastName(paciente.getUser().getLastName())
            .email(paciente.getUser().getEmail())
            .phone(paciente.getUser().getPhone())
            .build())
        .build();
  }
}
