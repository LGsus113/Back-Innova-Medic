package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.especialidades.ActualizarEspecialidadDTO;
import com.DW2.InnovaMedic.dto.especialidades.RegistrarEspecialidadDTO;
import com.DW2.InnovaMedic.dto.especialidades.ResumenEspecialidadParaAdminDTO;
import com.DW2.InnovaMedic.dto.especialidades.ResumenEspecialidadParaPacienteDTO;
import com.DW2.InnovaMedic.entity.CategoriaEspecialidad;
import com.DW2.InnovaMedic.entity.Especialidad;
import com.DW2.InnovaMedic.repository.CategoriaEspecialidadRepository;
import com.DW2.InnovaMedic.repository.EspecialidadRepository;
import com.DW2.InnovaMedic.service.MaintenanceEspecialidades;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenanceEspecialidadesImpl implements MaintenanceEspecialidades {
    private final EspecialidadRepository especialidadRepository;
    private final CategoriaEspecialidadRepository categoriaEspecialidadRepository;

    private Especialidad obtenerEspecialidadPorId(Integer id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada con ID: " + id));
    }

    private CategoriaEspecialidad obtenerCategoriaPorId(Integer id) {
        return categoriaEspecialidadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
    }

    private ResumenEspecialidadParaAdminDTO convertirAdmin(Especialidad e) {
        return new ResumenEspecialidadParaAdminDTO(
                e.getId(),
                e.getNombreEspecialidad(),
                e.getDescripcion(),
                e.getVisible()
        );
    }

    private ResumenEspecialidadParaPacienteDTO convertirPaciente(Especialidad e) {
        return new ResumenEspecialidadParaPacienteDTO(
                e.getId(),
                e.getNombreEspecialidad()
        );
    }

    private void exceptionState(List<Especialidad> visibles, String mensaje) {
        if (visibles.isEmpty()) {
            throw new IllegalStateException(mensaje);
        }
    }

    @Override
    public List<ResumenEspecialidadParaAdminDTO> listarEspecialidadesAdmin() {
        List<Especialidad> visibles = especialidadRepository.findAll();
        exceptionState(visibles,"No se encontraron especialidades disponibles");

        return visibles.stream()
                .map(this::convertirAdmin)
                .collect(toList());
    }

    @Override
    public List<ResumenEspecialidadParaPacienteDTO> listarEspecialidadesParaPaciente() {
        List<Especialidad> visibles = especialidadRepository.findByVisibleTrue();
        exceptionState(visibles,"No se encontraron especialidades disponibles");

        return visibles.stream()
                .map(this::convertirPaciente)
                .collect(toList());
    }

    @Override
    public void registrarOReactivarEspecialidad(RegistrarEspecialidadDTO registrarEspecialidadDTO) {
        Optional<Especialidad> existente = especialidadRepository.findByNombreEspecialidad(registrarEspecialidadDTO.nombreEspecialidad());

        if (existente.isPresent()) {
            Especialidad especialidad = existente.get();
            if (especialidad.getVisible()) {
                throw new IllegalArgumentException("Ya existe una especialidad con ese nombre y está activa.");
            }

            especialidad.setVisible(true);
            especialidadRepository.save(especialidad);
            return;
        }

        Especialidad nueva = new Especialidad();
        nueva.setNombreEspecialidad(registrarEspecialidadDTO.nombreEspecialidad());
        nueva.setDescripcion(registrarEspecialidadDTO.descripcion());
        nueva.setVisible(true);
        nueva.setCategoria(obtenerCategoriaPorId(registrarEspecialidadDTO.idCategoria()));

        especialidadRepository.save(nueva);
    }

    @Override
    public void actualizarEspecialidad(ActualizarEspecialidadDTO actualizarEspecialidadDTO) {
        Especialidad especialidad = obtenerEspecialidadPorId(actualizarEspecialidadDTO.id());

        if (actualizarEspecialidadDTO.nombreEspecialidad() != null) {
            if (especialidadRepository.existsByNombreEspecialidadAndIdNot(actualizarEspecialidadDTO.nombreEspecialidad(), actualizarEspecialidadDTO.id())) {
                throw new IllegalArgumentException("Ya existe otra especialidad con el nombre: " + actualizarEspecialidadDTO.nombreEspecialidad());
            }
            especialidad.setNombreEspecialidad(actualizarEspecialidadDTO.nombreEspecialidad());
        }

        if (actualizarEspecialidadDTO.descripcion() != null) {
            especialidad.setDescripcion(actualizarEspecialidadDTO.descripcion());
        }

        if (actualizarEspecialidadDTO.idCategoria() != null) {
            especialidad.setCategoria(obtenerCategoriaPorId(actualizarEspecialidadDTO.idCategoria()));
        }

        especialidadRepository.save(especialidad);
    }

    @Override
    public Integer contarMedicosDeEspecialidad(Integer idEspecialidad) {
        if (!especialidadRepository.existsById(idEspecialidad)) {
            throw new IllegalArgumentException("Especialidad no encontrada con ID: " + idEspecialidad);
        }
        return especialidadRepository.contarMedicosPorEspecialidad(idEspecialidad);
    }

    @Override
    public void eliminarEspecialidadPorId(Integer id) {
        Especialidad especialidad = obtenerEspecialidadPorId(id);

        if (!especialidad.getVisible()) {
            throw new IllegalStateException("La especialidad ya ha sido eliminada previamente.");
        }

        int cantidadMedicos = contarMedicosDeEspecialidad(id);
        if (cantidadMedicos > 0) {
            throw new IllegalStateException("No se puede eliminar la especialidad: aún hay " + cantidadMedicos + " médico(s) afiliado(s).");
        }

        especialidad.setVisible(false);
        especialidadRepository.save(especialidad);
    }
}
