package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.RegistroClinicoRequest;
import com.fisioterapia.fisioterapia_backend.dto.RegistroClinicoResponse;
import com.fisioterapia.fisioterapia_backend.entity.Fisioterapeuta;
import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import com.fisioterapia.fisioterapia_backend.entity.RegistroClinico;
import com.fisioterapia.fisioterapia_backend.repository.FisioterapeutaRepository;
import com.fisioterapia.fisioterapia_backend.repository.PacienteRepository;
import com.fisioterapia.fisioterapia_backend.repository.RegistroClinicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegistroClinicoService {

    private final RegistroClinicoRepository registroClinicoRepository;
    private final PacienteRepository pacienteRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;

    @Transactional
    public RegistroClinicoResponse crearRegistro(RegistroClinicoRequest request, Long fisioterapeutaUserId) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        String codigo = generarCodigoRegistro();

        RegistroClinico registro = new RegistroClinico();
        registro.setCodigo(codigo);
        registro.setPaciente(paciente);
        registro.setFisioterapeuta(fisioterapeuta);
        registro.setFechaRegistro(request.getFechaRegistro());
        registro.setDiagnostico(request.getDiagnostico());
        registro.setTratamiento(request.getTratamiento());
        registro.setObservaciones(request.getObservaciones());
        registro.setProximaSesion(request.getProximaSesion());

        registro = registroClinicoRepository.save(registro);
        return convertirAResponse(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroClinicoResponse> obtenerRegistrosPorPaciente(Long pacienteId) {
        return registroClinicoRepository.findByPacienteIdOrderByFechaRegistroDesc(pacienteId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RegistroClinicoResponse> obtenerRegistrosPorFisioterapeuta(Long fisioterapeutaUserId) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        return registroClinicoRepository.findByFisioterapeutaId(fisioterapeuta.getId())
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private String generarCodigoRegistro() {
        String codigo;
        do {
            long count = registroClinicoRepository.count();
            codigo = String.format("REC-%03d", count + 1);
        } while (registroClinicoRepository.existsByCodigo(codigo));

        return codigo;
    }

    private RegistroClinicoResponse convertirAResponse(RegistroClinico registro) {
        // Crear objeto anidado de fisioterapeuta
        RegistroClinicoResponse.FisioterapeutaInfo fisioterapeutaInfo = RegistroClinicoResponse.FisioterapeutaInfo.builder()
                .id(registro.getFisioterapeuta().getId())
                .especialidad(registro.getFisioterapeuta().getEspecialidad())
                .user(RegistroClinicoResponse.UserInfo.builder()
                        .id(registro.getFisioterapeuta().getUser().getId())
                        .firstName(registro.getFisioterapeuta().getUser().getFirstName())
                        .lastName(registro.getFisioterapeuta().getUser().getLastName())
                        .email(registro.getFisioterapeuta().getUser().getEmail())
                        .build())
                .build();

        return RegistroClinicoResponse.builder()
                .id(registro.getId())
                .codigo(registro.getCodigo())
                .pacienteNombre(registro.getPaciente().getUser().getFirstName() + " " + registro.getPaciente().getUser().getLastName())
                .pacienteCodigo(registro.getPaciente().getCodigo())
                .fisioterapeutaNombre("Dr. " + registro.getFisioterapeuta().getUser().getFirstName() + " " + registro.getFisioterapeuta().getUser().getLastName())
                .fecha(registro.getFechaRegistro())  // Alias para compatibilidad con frontend
                .fechaRegistro(registro.getFechaRegistro())
                .diagnostico(registro.getDiagnostico())
                .tratamiento(registro.getTratamiento())
                .observaciones(registro.getObservaciones())
                .proximaSesion(registro.getProximaSesion())
                .fisioterapeuta(fisioterapeutaInfo)
                .build();
    }
}
