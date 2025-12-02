package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.CitaRequest;
import com.fisioterapia.fisioterapia_backend.dto.CitaResponse;
import com.fisioterapia.fisioterapia_backend.dto.CitaUpdateRequest;
import com.fisioterapia.fisioterapia_backend.entity.Cita;
import com.fisioterapia.fisioterapia_backend.entity.Fisioterapeuta;
import com.fisioterapia.fisioterapia_backend.entity.Paciente;
import com.fisioterapia.fisioterapia_backend.repository.CitaRepository;
import com.fisioterapia.fisioterapia_backend.repository.FisioterapeutaRepository;
import com.fisioterapia.fisioterapia_backend.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final EmailService emailService;

    @Transactional
    public CitaResponse crearCita(CitaRequest request, Long pacienteUserId) {
        // Buscar paciente por user ID
        Paciente paciente = pacienteRepository.findByUserId(pacienteUserId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Buscar fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(request.getFisioterapeutaId())
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        // Crear código único para la cita
        String codigo = generarCodigoCita();

        // Crear cita
        Cita cita = new Cita();
        cita.setCodigo(codigo);
        cita.setPaciente(paciente);
        cita.setFisioterapeuta(fisioterapeuta);
        cita.setFecha(request.getFecha());
        cita.setHora(request.getHora());
        cita.setMotivo(request.getMotivo());
        cita.setEstado(Cita.EstadoCita.PROGRAMADA);

        cita = citaRepository.save(cita);

        // Enviar correos de confirmación
        emailService.enviarConfirmacionCita(
                cita,
                paciente.getUser().getEmail(),
                fisioterapeuta.getUser().getEmail()
        );

        return convertirAResponse(cita);
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerTodasCitas() {
        return citaRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPorPaciente(Long pacienteUserId) {
        Paciente paciente = pacienteRepository.findByUserId(pacienteUserId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return citaRepository.findByPacienteId(paciente.getId())
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPorPacienteId(Long pacienteId) {
        // Verificar que el paciente existe
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        return citaRepository.findByPacienteId(pacienteId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPorFisioterapeuta(Long fisioterapeutaUserId) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        return citaRepository.findByFisioterapeutaId(fisioterapeuta.getId())
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasPorEstado(Long pacienteUserId, String estado) {
        Paciente paciente = pacienteRepository.findByUserId(pacienteUserId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Cita.EstadoCita estadoCita = Cita.EstadoCita.valueOf(estado.toUpperCase());

        return citaRepository.findByPacienteIdAndEstado(paciente.getId(), estadoCita)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CitaResponse actualizarCita(Long citaId, CitaUpdateRequest request, Long pacienteUserId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Verificar que la cita pertenece al paciente
        Paciente paciente = pacienteRepository.findByUserId(pacienteUserId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (!cita.getPaciente().getId().equals(paciente.getId())) {
            throw new RuntimeException("No tienes permiso para modificar esta cita");
        }

        // Solo se pueden modificar citas PROGRAMADAS
        if (!cita.getEstado().equals(Cita.EstadoCita.PROGRAMADA)) {
            throw new RuntimeException("Solo se pueden modificar citas programadas");
        }

        // Actualizar datos
        if (request.getFisioterapeutaId() != null) {
            Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(request.getFisioterapeutaId())
                    .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));
            cita.setFisioterapeuta(fisioterapeuta);
        }

        cita.setFecha(request.getFecha());
        cita.setHora(request.getHora());
        if (request.getMotivo() != null) {
            cita.setMotivo(request.getMotivo());
        }

        cita = citaRepository.save(cita);

        // Enviar correos de actualización
        emailService.enviarConfirmacionCita(
                cita,
                cita.getPaciente().getUser().getEmail(),
                cita.getFisioterapeuta().getUser().getEmail()
        );

        return convertirAResponse(cita);
    }

    @Transactional
    public void cancelarCita(Long citaId, Long pacienteUserId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Verificar que la cita pertenece al paciente
        Paciente paciente = pacienteRepository.findByUserId(pacienteUserId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (!cita.getPaciente().getId().equals(paciente.getId())) {
            throw new RuntimeException("No tienes permiso para cancelar esta cita");
        }

        // Solo se pueden cancelar citas PROGRAMADAS
        if (!cita.getEstado().equals(Cita.EstadoCita.PROGRAMADA)) {
            throw new RuntimeException("Solo se pueden cancelar citas programadas");
        }

        cita.setEstado(Cita.EstadoCita.CANCELADA);
        citaRepository.save(cita);

        // Enviar notificación de cancelación
        emailService.enviarNotificacionCancelacion(
                cita,
                cita.getPaciente().getUser().getEmail(),
                cita.getFisioterapeuta().getUser().getEmail()
        );
    }

    @Transactional
    public CitaResponse completarCita(Long citaId, Long fisioterapeutaUserId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Verificar que la cita pertenece al fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        if (!cita.getFisioterapeuta().getId().equals(fisioterapeuta.getId())) {
            throw new RuntimeException("No tienes permiso para completar esta cita");
        }

        cita.setEstado(Cita.EstadoCita.COMPLETADA);
        cita = citaRepository.save(cita);

        return convertirAResponse(cita);
    }

    private String generarCodigoCita() {
        String codigo;
        do {
            long count = citaRepository.count();
            codigo = String.format("APT-%03d", count + 1);
        } while (citaRepository.existsByCodigo(codigo));

        return codigo;
    }

    private CitaResponse convertirAResponse(Cita cita) {
        int edad = 0;
        if (cita.getPaciente().getFechaNacimiento() != null) {
            edad = Period.between(cita.getPaciente().getFechaNacimiento(), LocalDate.now()).getYears();
        }

        // Crear objeto anidado de fisioterapeuta
        CitaResponse.FisioterapeutaInfo fisioterapeutaInfo = CitaResponse.FisioterapeutaInfo.builder()
                .id(cita.getFisioterapeuta().getId())
                .especialidad(cita.getFisioterapeuta().getEspecialidad())
                .user(CitaResponse.UserInfo.builder()
                        .id(cita.getFisioterapeuta().getUser().getId())
                        .firstName(cita.getFisioterapeuta().getUser().getFirstName())
                        .lastName(cita.getFisioterapeuta().getUser().getLastName())
                        .email(cita.getFisioterapeuta().getUser().getEmail())
                        .build())
                .build();

        // Crear objeto anidado de paciente
        CitaResponse.PacienteInfo pacienteInfo = CitaResponse.PacienteInfo.builder()
                .id(cita.getPaciente().getId())
                .codigo(cita.getPaciente().getCodigo())
                .user(CitaResponse.UserInfo.builder()
                        .id(cita.getPaciente().getUser().getId())
                        .firstName(cita.getPaciente().getUser().getFirstName())
                        .lastName(cita.getPaciente().getUser().getLastName())
                        .email(cita.getPaciente().getUser().getEmail())
                        .build())
                .build();

        return CitaResponse.builder()
                .id(cita.getId())
                .codigo(cita.getCodigo())
                .pacienteNombre(cita.getPaciente().getUser().getFirstName() + " " + cita.getPaciente().getUser().getLastName())
                .pacienteCodigo(cita.getPaciente().getCodigo())
                .fisioterapeutaNombre("Dr. " + cita.getFisioterapeuta().getUser().getFirstName() + " " + cita.getFisioterapeuta().getUser().getLastName())
                .especialidad(cita.getFisioterapeuta().getEspecialidad())
                .fecha(cita.getFecha())
                .hora(cita.getHora())
                .motivo(cita.getMotivo())
                .ubicacion(cita.getUbicacion())
                .estado(cita.getEstado().toString())
                .notas(cita.getNotas())
                // Agregar objetos anidados
                .fisioterapeuta(fisioterapeutaInfo)
                .paciente(pacienteInfo)
                .build();
    }
}
