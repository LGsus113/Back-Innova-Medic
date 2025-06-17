package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.SlotDTO;
import com.DW2.InnovaMedic.dto.SlotRequestDTO;

import java.util.List;

public interface MaintenanceDisponibilidadMedica {
    List<SlotDTO> obtenerSlotsDisponibles(SlotRequestDTO slotRequestDTO) throws Exception;
}
