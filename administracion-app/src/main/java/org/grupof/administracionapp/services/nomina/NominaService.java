package org.grupof.administracionapp.services.nomina;

import org.grupof.administracionapp.dto.nominas.NominaDTO;
import org.grupof.administracionapp.entity.nomina.Nomina;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface NominaService {
    void crearNomina(NominaDTO nomina);
    NominaDTO devuelveNominaPorEmpleadoId(UUID emp, UUID nom);
}
