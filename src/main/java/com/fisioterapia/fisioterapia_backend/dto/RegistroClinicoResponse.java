package com.fisioterapia.fisioterapia_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroClinicoResponse {
    private Long id;
    private String codigo;
    private String pacienteNombre;
    private String pacienteCodigo;
    private String fisioterapeutaNombre;
    private LocalDate fecha;  // Alias para fechaRegistro
    private LocalDate fechaRegistro;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private LocalDate proximaSesion;
    private FisioterapeutaInfo fisioterapeuta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FisioterapeutaInfo {
        private Long id;
        private String especialidad;
        private UserInfo user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
    }
}
