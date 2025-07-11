package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.registro.PacienteRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Paciente;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.PacienteRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenancePaciente;
import com.DW2.InnovaMedic.util.specifications.CitaSpecificationBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static com.DW2.InnovaMedic.util.UserUtil.responseCitas;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintanancePacienteImpl implements MaintenancePaciente {
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registrarPaciente(PacienteRegistroDTO pacienteRegistroDTO) {
        usuarioRepository.findOneByEmail(pacienteRegistroDTO.email())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + pacienteRegistroDTO.email());
                });

        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteRegistroDTO.nombre());
        paciente.setApellido(pacienteRegistroDTO.apellido());
        paciente.setSexo(pacienteRegistroDTO.sexo());
        paciente.setTelefono(pacienteRegistroDTO.telefono());
        paciente.setEmail(pacienteRegistroDTO.email());
        paciente.setContrasenia(passwordEncoder.encode(pacienteRegistroDTO.contrasenia()));
        paciente.setFechaNacimiento(pacienteRegistroDTO.fechaNacimiento());
        paciente.setTalla(pacienteRegistroDTO.talla());
        paciente.setGrupoSanguineo(pacienteRegistroDTO.grupoSanguineo());
        paciente.setDireccion(pacienteRegistroDTO.direccion());

        pacienteRepository.save(paciente);
    }

    @Override
    @Cacheable(value = "citasPaciente")
    public List<CitaDTO> obtenerCitasPaciente(Integer id, String nombreUsuario, Cita.Estado estado) {
        if (!pacienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Paciente con Id " + id + " no existe");
        }

        Specification<Cita> spec = CitaSpecificationBuilder.filterCitas(id, estado, nombreUsuario, CitaSpecificationBuilder.TipoBusqueda.PACIENTE);
        List<Cita> citas = citaRepository.findAll(spec);

        return responseCitas(citas);
    }
}
