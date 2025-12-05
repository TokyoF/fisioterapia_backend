package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.PacienteResponse;
import com.fisioterapia.fisioterapia_backend.service.PacienteService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

  private final PacienteService pacienteService;

  // Paciente actual (usuario logueado)
  @GetMapping("/me")
  public ResponseEntity<PacienteResponse> obtenerPacienteActual(Authentication authentication) {
    return pacienteService.obtenerPacienteActual(authentication)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());  // 404 si no hay paciente asignado
  }

  // Paciente por id
  @GetMapping("/{pacienteId}")
  public ResponseEntity<PacienteResponse> obtenerPacientePorId(@PathVariable Long pacienteId) {
    return pacienteService.obtenerPorId(pacienteId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  // Listar todos los pacientes
  @GetMapping
  public ResponseEntity<List<PacienteResponse>> listarPacientes() {
    return ResponseEntity.ok(pacienteService.listarTodos());
  }
}

