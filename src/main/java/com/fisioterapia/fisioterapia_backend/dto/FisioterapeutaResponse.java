package com.fisioterapia.fisioterapia_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FisioterapeutaResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String especialidad;
    private String numeroLicencia;
    private String email;
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
