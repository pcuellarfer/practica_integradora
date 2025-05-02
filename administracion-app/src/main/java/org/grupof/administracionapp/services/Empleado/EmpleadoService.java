package org.grupof.administracionapp.services.Empleado;

import java.util.UUID;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio encargado de gestionar operaciones relacionadas con empleados.
 * Define los métodos necesarios para registrar, editar, eliminar,
 * buscar y listar empleados dentro del sistema.
 */
@Service
public interface EmpleadoService {

    /**
     * Registra un nuevo empleado en el sistema a partir de los datos personales y empresariales,
     * asociados a un usuario existente.
     *
     * @param registroEmpleadoDTO Objeto con los datos del empleado a registrar.
     * @param usuarioDTO Objeto del usuario asociado al nuevo empleado.
     */
    void registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO, UsuarioDTO usuarioDTO);

    /**
     * Edita los datos de un empleado existente.
     *
     * @param id Identificador único del empleado a editar.
     * @param dto Objeto con los nuevos datos del empleado.
     * @return El DTO del empleado actualizado.
     */
    RegistroEmpleadoDTO editarEmpleado(UUID id, RegistroEmpleadoDTO dto);

    /**
     * Elimina lógicamente un empleado del sistema (sin eliminarlo físicamente de la base de datos).
     *
     * @param id Identificador único del empleado a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    boolean eliminarEmpleado(UUID id);

    /**
     * Busca un empleado por su identificador único.
     *
     * @param id Identificador único del empleado.
     * @return El DTO del empleado encontrado, o null si no existe.
     */
    RegistroEmpleadoDTO buscarEmpleado(UUID id);

    /**
     * Obtiene una lista de todos los empleados registrados en el sistema.
     *
     * @return Lista de DTOs de todos los empleados.
     */
    List<RegistroEmpleadoDTO> listarEmpleados();

    /**
     * Busca un empleado por el identificador del usuario al que está asociado.
     *
     * @param usuarioId Identificador único del usuario.
     * @return El DTO del empleado correspondiente al usuario, o null si no existe.
     */
    RegistroEmpleadoDTO buscarEmpleadoPorUsuarioId(UUID usuarioId);

    /**
     * Bloquea un empleado especificado por su ID.
     * <p>
     * Este método se encarga de cambiar el estado del usuario asociado al empleado a "bloqueado".
     * Si el empleado existe y tiene un usuario asociado, se actualiza el estado de bloqueo
     * del usuario y se guarda la entidad. En caso contrario, se lanza una excepción.
     *
     * @param empleadoId el identificador único del empleado cuyo estado se desea bloquear
     * @throws RuntimeException si no se encuentra al empleado o si no tiene un usuario asociado
     */
    void bloquearEmpleado(UUID empleadoId);

    /**
     * Desbloquea un empleado especificado por su ID.
     * <p>
     * Este método se encarga de cambiar el estado del usuario asociado al empleado a "desbloqueado".
     * Si el empleado existe y tiene un usuario asociado, se actualiza el estado de bloqueo
     * del usuario y se guarda la entidad. En caso contrario, se lanza una excepción.
     *
     * @param empleadoId el identificador único del empleado cuyo estado se desea desbloquear
     * @throws RuntimeException si no se encuentra al empleado o si no tiene un usuario asociado
     */
    void desbloquearEmpleado(UUID empleadoId);

}
