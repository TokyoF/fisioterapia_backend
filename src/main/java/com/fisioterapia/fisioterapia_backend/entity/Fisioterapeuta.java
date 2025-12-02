package com.fisioterapia.fisioterapia_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fisioterapeutas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fisioterapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, length = 100)
    private String especialidad; // Rehabilitación Deportiva, Terapia Manual, etc.

    @Column(length = 50)
    private String numeroLicencia;

    @Column(nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;

    @OneToMany(mappedBy = "fisioterapeuta", cascade = CascadeType.ALL)
    private List<Horario> horarios;

    @OneToMany(mappedBy = "fisioterapeuta", cascade = CascadeType.ALL)
    private List<Cita> citas;

    @OneToMany(mappedBy = "fisioterapeuta", cascade = CascadeType.ALL)
    private List<RegistroClinico> registrosClinicos;
}
