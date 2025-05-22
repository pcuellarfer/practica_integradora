package org.grupof.administracionapp.services.Especialidades;

import org.grupof.administracionapp.entity.registroEmpleado.Especialidad;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Interfaz que define los métodos para la gestión de especialidades en la aplicación.
 * Proporciona operaciones para obtener información sobre especialidades registradas.
 */
public interface EspecialidadesService {
    /**
     * Recupera todas las especialidades registradas en el sistema.
     *
     * @return una lista con todas las entidades {@link Especialidad}.
     */
    List<Especialidad> getAllEspecialidades();

    /**
     * Recupera una especialidad por su identificador único.
     *
     * @param id el UUID de la especialidad que se desea obtener.
     * @return la entidad {@link Especialidad} correspondiente al identificador proporcionado.
     * @throws NoSuchElementException si no se encuentra una especialidad con el ID dado.
     */
    Especialidad getEspecialidadById(UUID id);

}
