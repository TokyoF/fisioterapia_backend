package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.RegistroClinicoRequest;
import com.fisioterapia.fisioterapia_backend.dto.RegistroClinicoResponse;
import com.fisioterapia.fisioterapia_backend.service.RegistroClinicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-clinicos")
@RequiredArgsConstructor
public class RegistroClinicoController {

    private final RegistroClinicoService registroClinicoService;
    private final com.fisioterapia.fisioterapia_backend.repository.UserRepository userRepository;

    @PostMapping
    public ResponseEntity<RegistroClinicoResponse> crearRegistro(
            @Valid @RequestBody RegistroClinicoRequest request,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        RegistroClinicoResponse response = registroClinicoService.crearRegistro(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<RegistroClinicoResponse>> obtenerRegistrosPorPaciente(
            @PathVariable Long pacienteId) {
        List<RegistroClinicoResponse> registros = registroClinicoService.obtenerRegistrosPorPaciente(pacienteId);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/fisioterapeuta")
    public ResponseEntity<List<RegistroClinicoResponse>> obtenerRegistrosFisioterapeuta(
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        List<RegistroClinicoResponse> registros = registroClinicoService.obtenerRegistrosPorFisioterapeuta(userId);
        return ResponseEntity.ok(registros);
    }
}
