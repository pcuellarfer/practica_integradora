package org.grupof.administracionapp.services.TipoDocumento;

import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;
import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los servicios para la gestión de tipos de documento.
 */
public interface TipoDocumentoService {

    /**
     * Obtiene una lista con todos los tipos de documento disponibles.
     *
     * @return una lista de objetos {@link TipoDocumento}.
     */
    List<TipoDocumento> getAllTipoDocumento();

    /**
     * Obtiene un tipo de documento a partir de su identificador único.
     *
     * @param id el UUID que identifica al tipo de documento.
     * @return el objeto {@link TipoDocumento} correspondiente, o {@code null} si no se encuentra.
     */
    TipoDocumento getTipoDocumentoById(UUID id);
}