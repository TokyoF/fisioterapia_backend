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
public class PacienteResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private Integer edad;
    private String genero;
    private String telefono;
    private String email;
    private String tipoSangre;
    private String alergias;
    private String antecedentes;
    private LocalDate fechaNacimiento;
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
    }
}
