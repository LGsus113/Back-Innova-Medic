package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;

public interface MaintenancePdfExportService {
    public byte[] exportarRecetaComoPDF(CitaDTO citaDTO) throws Exception;
}
