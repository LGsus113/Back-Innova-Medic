package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.CitaRecetaVaciaDTO;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.service.MaintenanceCita;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;

@Service
@Transactional
public class MaintenanceCitaImpl implements MaintenanceCita {
    @Autowired
    CitaRepository citaRepository;

    @Override
    public Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception {
        if (citaRecetaVaciaDTO.fecha() == null || citaRecetaVaciaDTO.hora() == null) {
            throw new IllegalArgumentException("Fecha y hora son requeridos");
        }

        Date fecha = Date.valueOf(citaRecetaVaciaDTO.fecha());
        Time hora = Time.valueOf(citaRecetaVaciaDTO.hora());

        return citaRepository.registrar_cita_con_receta_vacia(
                citaRecetaVaciaDTO.idMedico(),
                citaRecetaVaciaDTO.idPaciente(),
                fecha,
                hora,
                citaRecetaVaciaDTO.tratamiento()
        );
    }
}
