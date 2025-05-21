package org.grupof.administracionapp.services.etiqueta;

import org.grupof.administracionapp.entity.Etiqueta;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz que define los métodos para la gestión de etiquetas en la aplicación.
 * Proporciona operaciones para guardar y buscar etiquetas asociadas a empleados.
 */
public interface EtiquetaService {
    /**
     * Guarda una etiqueta en el sistema.
     *
     * @param etiqueta la entidad {@link Etiqueta} que se desea guardar.
     */
    void guardarEtiqueta(Etiqueta etiqueta);

    /**
     * Busca una lista de etiquetas a partir de una lista de identificadores.
     *
     * @param ids una lista de UUIDs correspondientes a las etiquetas a buscar.
     * @return una lista de etiquetas que coinciden con los identificadores proporcionados.
     */
    List<Etiqueta> buscarPorIds(List<UUID> ids);

    /**
     * Busca todas las etiquetas asociadas a un empleado específico.
     *
     * @param empleadoId el UUID del empleado cuyas etiquetas se desean recuperar.
     * @return una lista de etiquetas vinculadas al empleado especificado.
     */
    List<Etiqueta> buscarPorEmpleadoId(UUID empleadoId);

    /**
     * Busca una etiqueta por su identificador único.
     *
     * @param id el UUID de la etiqueta que se desea buscar.
     * @return un {@link Optional} que contiene la etiqueta si se encuentra, o vacío si no existe.
     */
    Optional<Etiqueta> buscarPorId(UUID id);

}
