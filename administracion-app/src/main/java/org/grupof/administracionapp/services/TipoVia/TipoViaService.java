package org.grupof.administracionapp.services.TipoVia;

import org.grupof.administracionapp.entity.registroEmpleado.TipoVia;
import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los servicios para la gestión de tipos de vía.
 */
public interface TipoViaService {

    /**
     * Obtiene una lista con todos los tipos de vía disponibles.
     *
     * @return una lista de objetos {@link TipoVia}.
     */
    List<TipoVia> getAllTipoVia();

    /**
     * Obtiene un tipo de vía a partir de su identificador único.
     *
     * @param id el UUID que identifica al tipo de vía.
     * @return el objeto {@link TipoVia} correspondiente, o {@code null} si no se encuentra.
     */
    TipoVia getTipoViaById(UUID id);
}

