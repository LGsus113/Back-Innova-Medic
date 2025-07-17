package com.DW2.InnovaMedic.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CATEGORIA_ESPECIALIDAD")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaEspecialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIA")
    private Integer id;

    @Column(name = "NOMBRE_CATEGORIA", nullable = false, unique = true, length = 100)
    private String nombreCategoria;
}
