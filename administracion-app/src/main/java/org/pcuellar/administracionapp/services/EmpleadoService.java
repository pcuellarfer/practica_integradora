package org.pcuellar.administracionapp.services;

import java.util.UUID;
import org.pcuellar.administracionapp.dto.EmpleadoDTO;

import java.util.List;

public interface EmpleadoService {
    EmpleadoDTO registrarEmpleado(EmpleadoDTO dto);
    EmpleadoDTO editarEmpleado(UUID id, EmpleadoDTO dto);
    boolean eliminarEmpleado(UUID id);
    EmpleadoDTO buscarEmpleado(UUID id);
    List<EmpleadoDTO> listarEmpleados();
}