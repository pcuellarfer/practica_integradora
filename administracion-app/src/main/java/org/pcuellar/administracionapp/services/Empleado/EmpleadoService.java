package org.pcuellar.administracionapp.services.Empleado;

import java.util.UUID;
import org.pcuellar.administracionapp.dto.Empleado.EmpleadoDTO;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmpleadoService {
    EmpleadoDTO registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO);
    EmpleadoDTO editarEmpleado(UUID id, EmpleadoDTO dto);
    boolean eliminarEmpleado(UUID id);
    EmpleadoDTO buscarEmpleado(UUID id);
    List<EmpleadoDTO> listarEmpleados();
}