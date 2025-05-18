package org.grupof.administracionapp.services.nomina;

import org.grupof.administracionapp.dto.nominas.BusquedaNominaDTO;
import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.entity.nomina.Nomina;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface NominaService {
    void crearNomina(NominaDTO nomina);
    NominaDTO devuelveNominaPorEmpleadoId(UUID emp, UUID nom);
    List<BusquedaNominaDTO> buscarNominas(UUID empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
}
