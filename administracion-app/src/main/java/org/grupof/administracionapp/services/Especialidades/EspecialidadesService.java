package org.grupof.administracionapp.services.Especialidades;

import org.grupof.administracionapp.entity.registroEmpleado.Especialidad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EspecialidadesService {
    List<Especialidad> getAllEspecialidades();
}
