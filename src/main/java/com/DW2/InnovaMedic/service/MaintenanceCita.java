package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.CitaRecetaVaciaDTO;

public interface MaintenanceCita {
    Integer registrarCitaVacia(CitaRecetaVaciaDTO citaRecetaVaciaDTO) throws Exception;
}
