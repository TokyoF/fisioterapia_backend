package com.fisioterapia.fisioterapia_backend.controller;

import com.fisioterapia.fisioterapia_backend.dto.AuthResponse;
import com.fisioterapia.fisioterapia_backend.dto.LoginRequest;
import com.fisioterapia.fisioterapia_backend.dto.RegisterRequest;
import com.fisioterapia.fisioterapia_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, "ROLE_ADMIN"));
    }

    @PostMapping("/register/fisioterapeuta")
    public ResponseEntity<AuthResponse> registerFisioterapeuta(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, "ROLE_FISIOTERAPEUTA"));
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<AuthResponse> registerCliente(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, "ROLE_CLIENTE"));
    }
}
