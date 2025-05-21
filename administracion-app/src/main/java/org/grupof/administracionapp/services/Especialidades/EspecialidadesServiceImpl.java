package org.grupof.administracionapp.services.Especialidades;

import org.grupof.administracionapp.entity.registroEmpleado.Especialidad;
import org.grupof.administracionapp.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio para la gestión de especialidades.
 * Proporciona métodos para obtener todas las especialidades y para obtener una especialidad
 * por su identificador único.
 */
@Service
public class EspecialidadesServiceImpl implements EspecialidadesService {

    private final EspecialidadRepository especialidadesRepository;

    /**
     * Constructor que inyecta el repositorio de especialidades.
     *
     * @param especialidadesRepository el repositorio para acceder a la persistencia de especialidades.
     */
    public EspecialidadesServiceImpl(EspecialidadRepository especialidadesRepository) {
        this.especialidadesRepository = especialidadesRepository;
    }

    /**
     * Obtiene una lista con todas las especialidades disponibles en la base de datos.
     *
     * @return una lista de objetos {@link Especialidad}.
     */
    @Override
    public List<Especialidad> getAllEspecialidades() {
        return especialidadesRepository.findAll();
    }

    /**
     * Obtiene una especialidad a partir de su identificador único.
     *
     * @param id el UUID que identifica a la especialidad.
     * @return el objeto {@link Especialidad} correspondiente, o {@code null} si no se encuentra.
     */
    @Override
    public Especialidad getEspecialidadById(UUID id) {
        return especialidadesRepository.findById(id).orElse(null);
    }
}

