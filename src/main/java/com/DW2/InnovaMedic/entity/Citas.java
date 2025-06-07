package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CITAS", uniqueConstraints = @UniqueConstraint(columnNames = {"ID_MEDICO", "FECHA", "HORA"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Citas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITAS")
    private Integer idCitas;

    @ManyToOne
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medicos medico;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private Paciente paciente;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "HORA", nullable = false)
    private LocalTime hora;

    @Column(name = "TRATAMIENTO")
    private String tratamiento;

    @Column(name = "NOTAS_MEDICAS", columnDefinition = "TEXT")
    private String notasMedicas;

    @Column(name = "DIAGNOSTICO")
    private String diagnostico;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private Estado estado = Estado.Pendiente;

    public enum Estado {
        Pendiente, Confirmada, Cancelada, Finalizada
    }
}
