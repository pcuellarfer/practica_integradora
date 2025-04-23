package org.grupof.administracionapp.services;

import org.grupof.administracionapp.dto.NominaDTO;

import java.util.List;
import java.util.UUID;

public interface NominaService {
    NominaDTO crearNomina(NominaDTO dto);
    List<NominaDTO> listarNominas();
    NominaDTO obtenerNominaPorId(UUID id);
    List<NominaDTO> obtenerNominasEmpleado(UUID empleadoId);
}
