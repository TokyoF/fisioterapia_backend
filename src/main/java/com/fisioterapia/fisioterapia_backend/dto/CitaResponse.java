package com.fisioterapia.fisioterapia_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {
    private Long id;
    private String codigo;
    private String pacienteNombre;
    private String pacienteCodigo;
    private String fisioterapeutaNombre;
    private String especialidad;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String ubicacion;
    private String estado;
    private String notas;
    
    // Objetos anidados para compatibilidad con el frontend
    private FisioterapeutaInfo fisioterapeuta;
    private PacienteInfo paciente;
    
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
    public static class PacienteInfo {
        private Long id;
        private String codigo;
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
