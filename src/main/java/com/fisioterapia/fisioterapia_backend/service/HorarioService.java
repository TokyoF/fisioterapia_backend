package com.fisioterapia.fisioterapia_backend.service;

import com.fisioterapia.fisioterapia_backend.dto.HorarioRequest;
import com.fisioterapia.fisioterapia_backend.dto.HorarioResponse;
import com.fisioterapia.fisioterapia_backend.entity.Fisioterapeuta;
import com.fisioterapia.fisioterapia_backend.entity.Horario;
import com.fisioterapia.fisioterapia_backend.repository.FisioterapeutaRepository;
import com.fisioterapia.fisioterapia_backend.repository.HorarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;

    @Transactional
    public HorarioResponse crearHorario(HorarioRequest request, Long fisioterapeutaUserId) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        Horario horario = new Horario();
        horario.setFisioterapeuta(fisioterapeuta);
        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        horario.setDisponible(request.getDisponible());

        horario = horarioRepository.save(horario);
        return convertirAResponse(horario);
    }

    @Transactional(readOnly = true)
    public List<HorarioResponse> obtenerHorariosPorFisioterapeuta(Long fisioterapeutaUserId) {
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        return horarioRepository.findByFisioterapeutaId(fisioterapeuta.getId())
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HorarioResponse> obtenerHorariosPorFisioterapeutaId(Long fisioterapeutaId) {
        // Verificar que el fisioterapeuta existe
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(fisioterapeutaId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        return horarioRepository.findByFisioterapeutaId(fisioterapeutaId)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HorarioResponse actualizarHorario(Long horarioId, HorarioRequest request, Long fisioterapeutaUserId) {
        Horario horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Verificar que el horario pertenece al fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        if (!horario.getFisioterapeuta().getId().equals(fisioterapeuta.getId())) {
            throw new RuntimeException("No tienes permiso para modificar este horario");
        }

        horario.setDiaSemana(request.getDiaSemana());
        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraFin(request.getHoraFin());
        if (request.getDisponible() != null) {
            horario.setDisponible(request.getDisponible());
        }
        
        horario = horarioRepository.save(horario);

        return convertirAResponse(horario);
    }

    @Transactional
    public HorarioResponse actualizarDisponibilidad(Long horarioId, Boolean disponible, Long fisioterapeutaUserId) {
        Horario horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Verificar que el horario pertenece al fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        if (!horario.getFisioterapeuta().getId().equals(fisioterapeuta.getId())) {
            throw new RuntimeException("No tienes permiso para modificar este horario");
        }

        horario.setDisponible(disponible);
        horario = horarioRepository.save(horario);

        return convertirAResponse(horario);
    }

    @Transactional
    public void eliminarHorario(Long horarioId, Long fisioterapeutaUserId) {
        Horario horario = horarioRepository.findById(horarioId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Verificar que el horario pertenece al fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findByUserId(fisioterapeutaUserId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        if (!horario.getFisioterapeuta().getId().equals(fisioterapeuta.getId())) {
            throw new RuntimeException("No tienes permiso para eliminar este horario");
        }

        horarioRepository.delete(horario);
    }

    private HorarioResponse convertirAResponse(Horario horario) {
        return HorarioResponse.builder()
                .id(horario.getId())
                .diaSemana(horario.getDiaSemana())
                .horaInicio(horario.getHoraInicio())
                .horaFin(horario.getHoraFin())
                .disponible(horario.getDisponible())
                .build();
    }
}
