package org.grupof.administracionapp.services.Especialidades;

import org.grupof.administracionapp.entity.registroEmpleado.Especialidad;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface EspecialidadesService {
    List<Especialidad> getAllEspecialidades();
    Especialidad getEspecialidadById(UUID id);
}
