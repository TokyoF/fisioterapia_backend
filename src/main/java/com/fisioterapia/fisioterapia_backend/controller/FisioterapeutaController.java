package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.FisioterapeutaResponse;
import com.fisioterapia.fisioterapia_backend.entity.User;
import com.fisioterapia.fisioterapia_backend.service.FisioterapeutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fisioterapeutas")
@RequiredArgsConstructor
public class FisioterapeutaController {

    private final FisioterapeutaService fisioterapeutaService;

    @GetMapping
    public ResponseEntity<List<FisioterapeutaResponse>> obtenerTodosFisioterapeutas() {
        List<FisioterapeutaResponse> fisioterapeutas = fisioterapeutaService.obtenerTodosActivos();
        return ResponseEntity.ok(fisioterapeutas);
    }

    @GetMapping("/me")
    public ResponseEntity<FisioterapeutaResponse> obtenerFisioterapeutaActual(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        FisioterapeutaResponse fisioterapeuta = fisioterapeutaService.obtenerPorUserId(user.getId());
        return ResponseEntity.ok(fisioterapeuta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FisioterapeutaResponse> obtenerFisioterapeutaPorId(@PathVariable Long id) {
        FisioterapeutaResponse fisioterapeuta = fisioterapeutaService.obtenerPorId(id);
        return ResponseEntity.ok(fisioterapeuta);
    }
}
