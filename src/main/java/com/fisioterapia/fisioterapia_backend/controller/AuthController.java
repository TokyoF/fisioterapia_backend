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
        return ResponseEntity.ok(authService.register(request, "ROLE_PACIENTE"));
    }

    @PostMapping("/register/paciente")
    public ResponseEntity<AuthResponse> registerPaciente(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, "ROLE_PACIENTE"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.sendPasswordResetEmail(email);
        return ResponseEntity.ok("Se ha enviado un código de verificación a tu correo electrónico");
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<String> verifyResetCode(@RequestParam String email, @RequestParam String code) {
        authService.verifyResetCode(email, code);
        return ResponseEntity.ok("Código verificado correctamente");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email, 
            @RequestParam String code, 
            @RequestParam String newPassword) {
        authService.resetPassword(email, code, newPassword);
        return ResponseEntity.ok("Contraseña restablecida exitosamente");
    }
}
