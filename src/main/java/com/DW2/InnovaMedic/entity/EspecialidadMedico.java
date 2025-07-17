package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ESPECIALIDAD_MEDICO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESPECIALIDAD")
    private Integer id;

    @Column(name = "NOMBRE_ESPECIALIDAD", nullable = false, unique = true, length = 100)
    private String nombreEspecialidad;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "VISIBLE", nullable = false)
    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA", nullable = false)
    private CategoriaEspecialidad categoria;
}
