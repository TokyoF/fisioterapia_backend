package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.PacienteResponse;
import com.fisioterapia.fisioterapia_backend.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final com.fisioterapia.fisioterapia_backend.repository.UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<PacienteResponse> obtenerPacienteActual(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        PacienteResponse paciente = pacienteService.obtenerPorUserId(userId);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<PacienteResponse> obtenerPacientePorId(@PathVariable Long pacienteId) {
        PacienteResponse paciente = pacienteService.obtenerPorId(pacienteId);
        return ResponseEntity.ok(paciente);
    }
}
