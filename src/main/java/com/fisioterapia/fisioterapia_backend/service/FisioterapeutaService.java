package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.FisioterapeutaResponse;
import com.fisioterapia.fisioterapia_backend.entity.Fisioterapeuta;
import com.fisioterapia.fisioterapia_backend.repository.FisioterapeutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FisioterapeutaService {

    private final FisioterapeutaRepository fisioterapeutaRepository;

    @Transactional(readOnly = true)
    public List<FisioterapeutaResponse> obtenerTodosActivos() {
        return fisioterapeutaRepository.findByActivoTrue()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FisioterapeutaResponse obtenerPorUserId(Long userId) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));
        return convertirAResponse(fisioterapeuta);
    }

    private FisioterapeutaResponse convertirAResponse(Fisioterapeuta fisioterapeuta) {
        return FisioterapeutaResponse.builder()
                .id(fisioterapeuta.getId())
                .codigo("FIS-" + String.format("%03d", fisioterapeuta.getId()))
                .nombre("Dr. " + fisioterapeuta.getUser().getFirstName() + " " + fisioterapeuta.getUser().getLastName())
                .especialidad(fisioterapeuta.getEspecialidad())
                .numeroLicencia(fisioterapeuta.getNumeroLicencia())
                .email(fisioterapeuta.getUser().getEmail())
                .user(FisioterapeutaResponse.UserInfo.builder()
                        .id(fisioterapeuta.getUser().getId())
                        .username(fisioterapeuta.getUser().getUsername())
                        .firstName(fisioterapeuta.getUser().getFirstName())
                        .lastName(fisioterapeuta.getUser().getLastName())
                        .email(fisioterapeuta.getUser().getEmail())
                        .phone(fisioterapeuta.getUser().getPhone())
                        .build())
                .build();
    }
}
