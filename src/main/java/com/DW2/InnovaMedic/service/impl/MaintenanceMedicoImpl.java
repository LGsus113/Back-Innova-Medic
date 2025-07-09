package com.DW2.InnovaMedic.service.impl;

import com.DW2.InnovaMedic.dto.cita.CitaDTO;
import com.DW2.InnovaMedic.dto.cita.MedicoSegunEspecialidadDTO;
import com.DW2.InnovaMedic.dto.registro.MedicoRegistroDTO;
import com.DW2.InnovaMedic.entity.Cita;
import com.DW2.InnovaMedic.entity.Medico;
import com.DW2.InnovaMedic.repository.CitaRepository;
import com.DW2.InnovaMedic.repository.MedicoRepository;
import com.DW2.InnovaMedic.repository.UsuarioRepository;
import com.DW2.InnovaMedic.service.MaintenanceMedico;
import com.DW2.InnovaMedic.util.specifications.CitaSpecificationBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import static com.DW2.InnovaMedic.util.UserUtil.responseCitas;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceMedicoImpl implements MaintenanceMedico {
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @CacheEvict(value = {"listaEspecialidades", "lstaMedicoPorEspecialidad"}, allEntries = true)
    public void registrarMedicos(MedicoRegistroDTO medicoRegistroDTO) {
        usuarioRepository.findOneByEmail(medicoRegistroDTO.email())
                .ifPresent(u -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario registrado con el email: " + medicoRegistroDTO.email());
                });

        Medico medico = new Medico();
        medico.setNombre(medicoRegistroDTO.nombre());
        medico.setApellido(medicoRegistroDTO.apellido());
        medico.setSexo(medicoRegistroDTO.sexo());
        medico.setTelefono(medicoRegistroDTO.telefono());
        medico.setEmail(medicoRegistroDTO.email());
        medico.setContrasenia(passwordEncoder.encode(medicoRegistroDTO.contrasenia()));
        medico.setEspecialidad(medicoRegistroDTO.especialidad());
        medico.setNumeroColegiado(medicoRegistroDTO.numeroColegiado());
        medico.setCodigoHospital(medicoRegistroDTO.codigoHospital());

        medicoRepository.save(medico);
    }

    @Override
    @Cacheable(value = "citasMedico")
    public List<CitaDTO> obtenerCitasMedico(Integer id, String nombreUsuario, Cita.Estado estado) {
        if (!medicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Medico con Id " + id + " no existe");
        }

        Specification<Cita> spec = CitaSpecificationBuilder.filterCitas(id, estado, nombreUsuario, CitaSpecificationBuilder.TipoBusqueda.MEDICO);
        List<Cita> citas = citaRepository.findAll(spec);

        return responseCitas(citas);
    }

    @Override
    @Cacheable(value = "listaEspecialidades")
    public List<String> obtenerEspecialidadesUnicas() {
        return medicoRepository.findAllDistinctEspecialidades();
    }

    @Override
    @Cacheable(value = "lstaMedicoPorEspecialidad")
    public List<MedicoSegunEspecialidadDTO> listarMedicosPorEspecialidad(String especialidad) {
        List<Medico> medicos = medicoRepository.findByEspecialidadIgnoreCase(especialidad);
        return medicos.stream()
                .map(m -> new MedicoSegunEspecialidadDTO(
                        m.getIdUsuario(),
                        m.getNombre(),
                        m.getApellido()
                ))
                .toList();
    }
}
