package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.CitaRequest;
import com.fisioterapia.fisioterapia_backend.dto.CitaResponse;
import com.fisioterapia.fisioterapia_backend.dto.CitaUpdateRequest;
import com.fisioterapia.fisioterapia_backend.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;
    private final com.fisioterapia.fisioterapia_backend.repository.UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<CitaResponse>> obtenerTodasCitas() {
        List<CitaResponse> citas = citaService.obtenerTodasCitas();
        return ResponseEntity.ok(citas);
    }

    @PostMapping
    public ResponseEntity<CitaResponse> crearCita(@Valid @RequestBody CitaRequest request, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        CitaResponse response = citaService.crearCita(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/paciente")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPaciente(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        List<CitaResponse> citas = citaService.obtenerCitasPorPaciente(userId);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPorPacienteId(@PathVariable Long pacienteId) {
        List<CitaResponse> citas = citaService.obtenerCitasPorPacienteId(pacienteId);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/fisioterapeuta")
    public ResponseEntity<List<CitaResponse>> obtenerCitasFisioterapeuta(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        List<CitaResponse> citas = citaService.obtenerCitasPorFisioterapeuta(userId);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/fisioterapeuta/{fisioterapeutaId}")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPorFisioterapeutaId(@PathVariable Long fisioterapeutaId) {
        List<CitaResponse> citas = citaService.obtenerCitasPorFisioterapeutaId(fisioterapeutaId);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/paciente/estado/{estado}")
    public ResponseEntity<List<CitaResponse>> obtenerCitasPorEstado(
            @PathVariable String estado,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        List<CitaResponse> citas = citaService.obtenerCitasPorEstado(userId, estado);
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/{citaId}")
    public ResponseEntity<CitaResponse> actualizarCita(
            @PathVariable Long citaId,
            @Valid @RequestBody CitaUpdateRequest request,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        CitaResponse response = citaService.actualizarCita(citaId, request, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{citaId}/cancelar")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long citaId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        citaService.cancelarCita(citaId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{citaId}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long citaId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        citaService.cancelarCita(citaId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{citaId}/completar")
    public ResponseEntity<CitaResponse> completarCita(@PathVariable Long citaId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        CitaResponse response = citaService.completarCita(citaId, userId);
        return ResponseEntity.ok(response);
    }
}
