package com.DW2.InnovaMedic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "SEGURO_PERSONAL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeguroPersonal {
    @Id
    @Column(name = "ID_SEGURO_PACIENTE")
    private Integer idSeguroPaciente;

    @Column(name = "NOMBRE_SEGURO", nullable = false)
    private String nombreSeguro;

    @Column(name = "NUMERO_POLIZA", unique = true, nullable = false)
    private Integer numeroPoliza;

    @Column(name = "TELEFONO_CONTACTO")
    private String telefonoContacto;

    @Column(name = "CORREO_CONTACTO", nullable = false)
    private String correoContacto;

    @Column(name = "DIRECCION_ASEGURADORA", nullable = false)
    private String direccionAseguradora;

    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    private LocalDate fechaVencimiento;
}
