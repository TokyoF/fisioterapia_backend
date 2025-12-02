package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.FisioterapeutaResponse;
import com.fisioterapia.fisioterapia_backend.service.FisioterapeutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
