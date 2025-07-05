package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import com.DW2.InnovaMedic.dto.slot.SlotRequestDTO;

import java.util.List;

public interface MaintenanceDisponibilidadMedica {
    List<SlotPorDiaDTO> obtenerSlotsDisponibles(SlotRequestDTO slotRequestDTO);
}
