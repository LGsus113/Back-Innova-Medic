package com.DW2.InnovaMedic.service;

import com.DW2.InnovaMedic.dto.especialidades.ActualizarEspecialidadDTO;
import com.DW2.InnovaMedic.dto.especialidades.RegistrarEspecialidadDTO;
import com.DW2.InnovaMedic.dto.especialidades.ResumenEspecialidadParaAdminDTO;
import com.DW2.InnovaMedic.dto.especialidades.ResumenEspecialidadParaPacienteDTO;
import java.util.List;

public interface MaintenanceEspecialidades {
    List<ResumenEspecialidadParaAdminDTO> listarEspecialidadesAdmin();
    List<ResumenEspecialidadParaPacienteDTO> listarEspecialidadesParaPaciente();
    void registrarOReactivarEspecialidad(RegistrarEspecialidadDTO registrarEspecialidadDTO);
    void actualizarEspecialidad(ActualizarEspecialidadDTO actualizarEspecialidadDTO);
    Integer contarMedicosDeEspecialidad(Integer idEspecialidad);
    void eliminarEspecialidadPorId(Integer id);
}
