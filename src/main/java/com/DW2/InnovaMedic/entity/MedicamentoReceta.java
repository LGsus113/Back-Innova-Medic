package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICAMENTOS_RECETA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentoReceta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICAMENTO")
    private Integer idMedicamento;

    @ManyToOne
    @JoinColumn(name = "ID_RECETA", nullable = false)
    private Receta receta;

    @Column(name = "NOMBRE_GENERICO", length = 100, nullable = false)
    private String nombreGenerico;

    @Column(name = "NOMBRE_FARMACEUTICO", length = 100, nullable = false)
    private String nombreFarmaceutico;

    @Column(name = "PRESENTACION", length = 50, nullable = false)
    private String presentacion;

    @Column(name = "VIA_ADMINISTRACION", length = 50, nullable = false)
    private String viaAdministracion;

    @Column(name = "DOSIS", length = 50, nullable = false)
    private String dosis;

    @Column(name = "FRECUENCIA", length = 100, nullable = false)
    private String frecuencia;

    @Column(name = "INDICACIONES_USO", columnDefinition = "TEXT")
    private String indicacionesUso;

    @Column(name = "DURACION_TRATAMIENTO", length = 50, nullable = false)
    private String duracionTratamiento;
}
