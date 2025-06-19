package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.perfil.DisponibilidadMedicaDTO;
import com.DW2.InnovaMedic.dto.perfil.MedicoPerfilDTO;
import com.DW2.InnovaMedic.dto.perfil.PacientePerfilDTO;
import com.DW2.InnovaMedic.entity.DisponibilidadMedica;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.entity.Usuario;
import com.DW2.InnovaMedic.repository.DisponibilidadMedicaRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MaintenanceUsuarioImpl implements MaintenanceUsuario {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DisponibilidadMedicaRepository disponibilidadMedicaRepository;

    @Override
    @Cacheable(value = "perfilUsuario", key = "#idUsuario")
    public Object obtenerUsuarioPorId(Integer idUsuario) throws Exception {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado."));

        if (usuario instanceof Medico medico) {
            List<DisponibilidadMedica> disponibilidades = disponibilidadMedicaRepository.findByMedico_IdUsuario(idUsuario);

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
                    medico.getEspecialidad(),
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
            throw new Exception("Tipo de usuario no reconocido");
        }
    }
}
