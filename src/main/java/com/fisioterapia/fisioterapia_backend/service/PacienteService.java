package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.PacienteResponse;
import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import com.fisioterapia.fisioterapia_backend.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorUserId(Long userId) {
        Paciente paciente = pacienteRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return convertirAResponse(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return convertirAResponse(paciente);
    }

    private PacienteResponse convertirAResponse(Paciente paciente) {
        // Calcular edad, manejando el caso donde fechaNacimiento puede ser null
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
