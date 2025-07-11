package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.slot.SlotPorDiaDTO;
import com.DW2.InnovaMedic.dto.projections.CitaHorarioProjection;
import com.DW2.InnovaMedic.dto.slot.SlotTimeDTO;
import com.DW2.InnovaMedic.entity.DisponibilidadMedica;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.DisponibilidadMedicaRepository;
import com.DW2.InnovaMedic.service.MaintenanceDisponibilidadMedica;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenanceDisponibilidadMedicaImpl implements MaintenanceDisponibilidadMedica {
    private final DisponibilidadMedicaRepository disponibilidadMedicaRepository;
    private final CitaRepository citaRepository;

    @Override
    @Cacheable(value = "slotsDisponibles", key = "#idMedico + '-' + #fechaInicioStr + '-' + #fechaFinStr")
    public List<SlotPorDiaDTO> obtenerSlotsDisponibles(Integer idMedico, String fechaInicioStr, String fechaFinStr) {
        if (idMedico == null || idMedico <= 0) {
            throw new IllegalArgumentException("El ID del médico es inválido");
        }

        if (fechaInicioStr == null || fechaFinStr == null || fechaInicioStr.isBlank() || fechaFinStr.isBlank()) {
            throw new IllegalArgumentException("Las fechas no pueden estar vacías");
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;
        try {
            fechaInicio = LocalDate.parse(fechaInicioStr);
            fechaFin = LocalDate.parse(fechaFinStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Las fechas deben tener el formato yyyy-MM-dd" + e.getMessage());
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        List<SlotPorDiaDTO> listaFinal = new ArrayList<>();

        List<DisponibilidadMedica> disponibilidadMedica = disponibilidadMedicaRepository
                .findByMedico_IdUsuario(idMedico);
        Map<DisponibilidadMedica.DiaSemana, DisponibilidadMedica> disponibilidadPorDia = disponibilidadMedica.stream()
                .collect(Collectors.toMap(DisponibilidadMedica::getDiaSemana, d -> d));

        List<CitaHorarioProjection> citasActivas = citaRepository.obtenerCitasActivasEnRango(idMedico, fechaInicio, fechaFin);

        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
            DayOfWeek dayOfWeek = fecha.getDayOfWeek();
            String nombreDia = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.of("es", "ES"));
            nombreDia = nombreDia.substring(0, 1).toUpperCase() + nombreDia.substring(1).toLowerCase();

            DisponibilidadMedica.DiaSemana diaSemanaEnum = convertirNombreDiaEnum(nombreDia);

            if (disponibilidadPorDia.containsKey(diaSemanaEnum)) {
                DisponibilidadMedica disponibilidad = disponibilidadPorDia.get(diaSemanaEnum);
                LocalTime inicio = disponibilidad.getHoraInicio();
                LocalTime fin = disponibilidad.getHoraFin();

                List<SlotTimeDTO> slotsDelDia = new ArrayList<>();

                while (!inicio.plusMinutes(30).isAfter(fin)) {
                    LocalTime slotInicio = inicio;
                    LocalTime slotFin = inicio.plusMinutes(30);

                    LocalDate finalFecha = fecha;
                    boolean ocupado = citasActivas.stream().anyMatch(cita -> {
                        if (!cita.getFecha().isEqual(finalFecha)) return false;

                        LocalTime citaInicio = cita.getHora();
                        LocalTime citaFin = citaInicio.plusHours(1);

                        return !(citaFin.isBefore(slotInicio) || citaInicio.isAfter(slotFin));
                    });

                    slotsDelDia.add(new SlotTimeDTO(
                            slotInicio.toString(),
                            slotFin.toString(),
                            !ocupado
                    ));

                    inicio = slotFin;
                }

                listaFinal.add(new SlotPorDiaDTO(
                        fecha,
                        nombreDia,
                        slotsDelDia
                ));
            }
        }

        return listaFinal;
    }

    private DisponibilidadMedica.DiaSemana convertirNombreDiaEnum(String nombreDia) {
        try {
            return DisponibilidadMedica.DiaSemana.valueOf(nombreDia);
        } catch (IllegalArgumentException iaex) {
            throw new IllegalArgumentException(nombreDia);
        }
    }
}

