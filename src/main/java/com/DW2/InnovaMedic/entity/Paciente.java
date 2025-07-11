package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "PACIENTES")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paciente extends Usuario {
    @OneToOne
    @JoinColumn(name = "ID_SEGURO")
    private SeguroPersonal seguroPersonal;

    @Column(name = "FECHA_NACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "TALLA", nullable = false)
    private String talla;

    @Column(name = "GRUPO_SANGUINEO", nullable = false)
    private String grupoSanguineo;

    @Column(name = "DIRECCION", nullable = false)
    private String direccion;
}
