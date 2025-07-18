package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.perfil.DisponibilidadMedicaDTO;
import com.DW2.InnovaMedic.dto.perfil.MedicoPerfilDTO;
import com.DW2.InnovaMedic.dto.perfil.PacientePerfilDTO;
import com.DW2.InnovaMedic.entity.*;
import com.DW2.InnovaMedic.repository.DisponibilidadMedicaRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenanceUsuarioImpl implements MaintenanceUsuario {
    private final UsuarioRepository usuarioRepository;
    private final DisponibilidadMedicaRepository disponibilidadMedicaRepository;

    @Override
    @Cacheable(value = "perfilUsuario", key = "#idUsuario")
    public Object obtenerUsuarioPorId(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario con id " + idUsuario +" no encontrado."));

        if (usuario instanceof Medico medico) {
            List<DisponibilidadMedica> disponibilidades = disponibilidadMedicaRepository.findByMedico_IdUsuario(idUsuario);

            List<String> nombresEspecialidad = medico.getEspecialidades().stream()
                    .map(Especialidad::getNombreEspecialidad)
                    .toList();

            List<DisponibilidadMedicaDTO> disponibilidadesDTO = disponibilidades.stream()
                    .map(d -> new DisponibilidadMedicaDTO(
                            d.getDiaSemana().name(),
                            d.getHoraInicio(),
                            d.getHoraFin()
                    ))
                    .toList();

            return new MedicoPerfilDTO(
                    medico.getSexo(),
                    medico.getTelefono(),
                    medico.getEmail(),
                    nombresEspecialidad,
                    medico.getNumeroColegiado(),
                    disponibilidadesDTO
            );
        } else if (usuario instanceof Paciente paciente) {
            return new PacientePerfilDTO(
                    paciente.getSexo(),
                    paciente.getTelefono(),
                    paciente.getEmail(),
                    paciente.getFechaNacimiento(),
                    paciente.getTalla(),
                    paciente.getGrupoSanguineo(),
                    paciente.getDireccion()
            );
        } else {
            throw new IllegalStateException("Tipo de usuario no reconocido");
        }
    }
}
