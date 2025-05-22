package org.grupof.administracionapp.services.TipoTarjetaService;

import org.grupof.administracionapp.entity.TipoTarjeta;
import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los servicios para la gestión de tipos de tarjeta.
 */
public interface TipoTarjetaService {

    /**
     * Obtiene una lista con todos los tipos de tarjeta disponibles.
     *
     * @return una lista de objetos {@link TipoTarjeta}.
     */
    List<TipoTarjeta> getAllTiposTarjetas();

    /**
     * Obtiene un tipo de tarjeta a partir de su identificador único.
     *
     * @param id el UUID que identifica al tipo de tarjeta.
     * @return el objeto {@link TipoTarjeta} correspondiente, o {@code null} si no se encuentra.
     */
    TipoTarjeta getTipoTarjetaById(UUID id);
}

