package org.grupof.administracionapp.services.Departamento;

import org.grupof.administracionapp.entity.registroEmpleado.Departamento;
import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define los métodos para la gestión de departamentos en la aplicación.
 * Proporciona operaciones para obtener información sobre departamentos registrados.
 */
public interface DepartamentoService {

    /**
     * Obtiene una lista con todos los departamentos registrados.
     *
     * @return lista de objetos {@link Departamento}
     */
    List<Departamento> getAllDepartamentos();

    /**
     * Busca un departamento por su identificador único.
     *
     * @param id el identificador UUID del departamento
     * @return el objeto {@link Departamento} correspondiente al ID, o {@code null} si no se encuentra
     */
    Departamento getDepartamentoById(UUID id);

    /**
     * Obtiene el nombre del departamento a partir de su ID.
     *
     * @param idDepartamento el identificador UUID del departamento
     * @return una cadena con el nombre del departamento si existe, o "Desconocido" si no se encuentra
     */
    String obtenerNombreDepartamento(UUID idDepartamento);
}