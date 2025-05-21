package org.grupof.administracionapp.services.Genero;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para la gestión de entidades de tipo {@link Genero}.
 * Proporciona operaciones para consultar géneros por ID o recuperar todos los disponibles.
 */
public interface GeneroService {

    /**
     * Recupera la lista completa de géneros disponibles en el sistema.
     *
     * @return una lista de objetos {@link Genero}
     */
    List<Genero> getAllGeneros();

    /**
     * Obtiene un género específico por su identificador único.
     *
     * @param id el identificador UUID del género
     * @return el objeto {@link Genero} correspondiente al ID
     * @throws EntityNotFoundException si no se encuentra un género con el ID especificado
     */
    Genero getGeneroById(UUID id);

    /**
     * Devuelve el nombre del género a partir de su ID.
     *
     * @param idGenero el identificador UUID del género
     * @return una cadena con el nombre del género, o null si no se encuentra
     */
    String obtenerNombreGenero(UUID idGenero);
}
