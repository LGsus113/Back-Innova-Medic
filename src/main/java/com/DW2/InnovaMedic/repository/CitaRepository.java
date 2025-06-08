package com.DW2.InnovaMedic.repository;

import com.DW2.InnovaMedic.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    @Procedure(procedureName = "registrar_cita_con_receta_vacia", outputParameterName = "p_id_cita_generada")
    Integer registrar_cita_con_receta_vacia(
            @Param("p_id_medico") Integer idMedico,
            @Param("p_id_paciente") Integer idPaciente,
            @Param("p_fecha") Date fecha,
            @Param("p_hora") Time hora,
            @Param("p_tratamiento") String tratamiento
    );

    List<Cita> findByPaciente_IdUsuario(Integer idUsuarioPaciente);
}
