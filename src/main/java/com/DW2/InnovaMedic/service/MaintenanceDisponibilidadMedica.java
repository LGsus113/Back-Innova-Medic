package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import java.util.List;

public interface MaintenanceDisponibilidadMedica {
    List<SlotPorDiaDTO> obtenerSlotsDisponibles(Integer idMedico, String fechaInicioStr, String fechaFinStr);
}
