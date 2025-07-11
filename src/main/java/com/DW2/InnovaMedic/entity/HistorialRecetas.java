package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "HISTORIAL_RECETAS")
@Data
public class HistorialRecetas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "URL_PDF", unique = true)
    private String url_pdf;

    @Column(name = "FECHA_GENERADO", nullable = false)
    private LocalDate fecha_generado;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_MEDICO", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "ID_CITAS", nullable = false)
    private Cita cita;
}
