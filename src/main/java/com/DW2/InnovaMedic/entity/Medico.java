package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MEDICOS")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medico extends Usuario {
    @Column(name = "NUMERO_COLEGIADO", nullable = false)
    private String numeroColegiado;

    @Column(name = "CODIGO_MEDICO_HOSPITAL", nullable = false, unique = true)
    private String codigoHospital;

    @ManyToMany
    @JoinTable(
            name = "MEDICO_ESPECIALIDAD",
            joinColumns = @JoinColumn(name = "ID_MEDICO"),
            inverseJoinColumns = @JoinColumn(name = "ID_ESPECIALIDAD")
    )
    private Set<Especialidad> especialidades = new HashSet<>();
}
