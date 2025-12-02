package com.fisioterapia.fisioterapia_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo; // Ej: PAT-001

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String genero; // M, F, Otro, etc.

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String direccion;

    @Column(length = 5)
    private String tipoSangre; // O+, A+, etc.

    @Column(columnDefinition = "TEXT")
    private String alergias; // JSON o texto separado por comas

    @Column(columnDefinition = "TEXT")
    private String antecedentes; // Historial médico previo

    @Column(nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @UpdateTimestamp
    private LocalDateTime actualizadoEn;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<RegistroClinico> registrosClinicos;
}
