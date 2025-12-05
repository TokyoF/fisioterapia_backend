package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.HorarioRequest;
import com.fisioterapia.fisioterapia_backend.dto.HorarioResponse;
import com.fisioterapia.fisioterapia_backend.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;
    private final com.fisioterapia.fisioterapia_backend.repository.UserRepository userRepository;

    @PostMapping
    public ResponseEntity<HorarioResponse> crearHorario(
            @Valid @RequestBody HorarioRequest request,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        HorarioResponse response = horarioService.crearHorario(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HorarioResponse>> obtenerHorarios(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        List<HorarioResponse> horarios = horarioService.obtenerHorariosPorFisioterapeuta(userId);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/fisioterapeuta/{fisioterapeutaId}")
    public ResponseEntity<List<HorarioResponse>> obtenerHorariosPorFisioterapeutaId(@PathVariable Long fisioterapeutaId) {
        List<HorarioResponse> horarios = horarioService.obtenerHorariosPorFisioterapeutaId(fisioterapeutaId);
        return ResponseEntity.ok(horarios);
    }

    @PutMapping("/{horarioId}")
    public ResponseEntity<HorarioResponse> actualizarHorario(
            @PathVariable Long horarioId,
            @Valid @RequestBody HorarioRequest request,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        HorarioResponse response = horarioService.actualizarHorario(horarioId, request, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{horarioId}/disponibilidad")
    public ResponseEntity<HorarioResponse> actualizarDisponibilidad(
            @PathVariable Long horarioId,
            @RequestParam Boolean disponible,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        HorarioResponse response = horarioService.actualizarDisponibilidad(horarioId, disponible, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{horarioId}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long horarioId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        horarioService.eliminarHorario(horarioId, userId);
        return ResponseEntity.noContent().build();
    }
}
