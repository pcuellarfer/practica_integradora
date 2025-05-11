package org.grupof.administracionapp.services.Nomina;

import org.grupof.administracionapp.dto.Nomina.NominaDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface NominaService {

    NominaDTO altaNomina(NominaDTO nominaDTO, UUID empleadoId);

    NominaDTO obtenerNominaPorId(UUID id);

    List<NominaDTO> obtenerNominasEmpleado(UUID empleadoId);

    void eliminarLineaNomina(UUID lineaNominaId);

    void eliminarNomina(UUID nominaId);

    UUID obtenerEmpleadoIdDeNomina(UUID nominaId);

    //List<NominaDTO> listarNominas();
    // NominaDTO modificaNomina(NominaDTO dto, UUID empleadoId);

}
