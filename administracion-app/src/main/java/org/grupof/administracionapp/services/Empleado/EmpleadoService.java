package org.grupof.administracionapp.services.Empleado;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.grupof.administracionapp.dto.Empleado.EmpleadoDetalleDTO;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Servicio encargado de gestionar operaciones relacionadas con empleados.
 * Define los métodos necesarios para registrar, editar, eliminar,
 * buscar y listar empleados dentro del sistema.
 */
@Service
public interface EmpleadoService {

    //usado en DetalleControler
    void guardarEmpleado(Empleado empleado);

    //usado en DetalleControler
    void actualizarDatosEmpleado(UUID usuarioId,
                                 RegistroEmpleadoDTO dto,
                                 MultipartFile foto,
                                 String uploadDir)
            throws IOException;

    //usado en DetalleControler
    EmpleadoDetalleDTO obtenerDetalleEmpleado(UUID usuarioId);

    //usado en DetalleControler
    RegistroEmpleadoDTO obtenerRegistroEmpleadoParaEdicion(UUID usuarioId);

    //usado en BusqedaEmpleadosController para la busqueda parametrizada nombre+genero
    List<Empleado> buscarEmpleados(String nombre, UUID generoId, List<UUID> departamentoIds,
                                   LocalDate fechaInicio, LocalDate fechaFin);


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
    RegistroEmpleadoDTO buscarRegistroEmpleado(UUID id);

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
     * Busca un empleado en base al ID de usuario asociado.
     *
     * @param usuarioId ID del usuario vinculado al empleado.
     * @return Un {@code Optional} que contiene el {@code Empleado} si se encuentra, o vacío si no.
     */
    Optional<Empleado> obtenerEmpleadoPorUsuarioId(UUID usuarioId);

    /**
     * Devuelve una lista de todos los empleados excepto el que tenga el ID proporcionado.
     *
     * Este método es útil para excluir al empleado que está realizando una acción,
     * como por ejemplo en la vista de asignación de subordinados.
     *
     * @param jefeId ID del empleado que se desea excluir.
     * @return Lista de empleados distintos al proporcionado.
     */
    List<Empleado>  buscarTodosMenosConJerarquia (UUID jefeId);

    /**
     * Busca una lista de empleados cuyos IDs se proporcionan.
     * <p>
     * Este método se utiliza normalmente para obtener los subordinados
     * asignados a un jefe a partir de sus identificadores.
     *
     * @param ids Lista de IDs de los empleados a buscar.
     * @return Lista de empleados correspondientes a los IDs dados.
     */
    List<Empleado> buscarPorIds(List<UUID> ids);

    /**
     * Guarda o actualiza en lote una lista de empleados.
     *
     * @param empleados Lista de empleados a guardar o actualizar.
     */
    void guardarTodos(List<Empleado> empleados);

    /**
     * Busca todos los empleados que tengan como jefe al empleado proporcionado.
     *
     * @param jefe Empleado que actúa como jefe.
     * @return Lista de empleados subordinados al jefe indicado.
     */
    List<Empleado> buscarPorJefe(Empleado jefe);

    /**
     * Busca un empleado por su identificador único.
     *
     * @param id ID del empleado a buscar.
     * @return Un {@code Optional} con el empleado encontrado, o vacío si no existe.
     */
    Optional<Empleado> buscarPorId(UUID id);

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
    void bloquearEmpleado(UUID empleadoId, String motivoBloqueo);

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

    /**
     * Obtiene una lista de todos los empleados ordenados por los criterios definidos
     * en la implementación del servicio. El criterio de orden puede ser por nombre,
     * fecha de incorporación, cargo, etc., según lo implementado.
     *
     * @return una lista ordenada de empleados.
     */
    List<Empleado> getEmpleadosOrdenados();

    /**
     * Obtiene el estado de bloqueo del usuario asociado al empleado con el ID proporcionado.
     *
     * @param empleadoId el identificador único del empleado.
     * @return {@code true} si el usuario está bloqueado; {@code false} si no lo está.
     * @throws RuntimeException si el empleado no existe o no tiene un usuario asociado.
     */
    boolean obtenerEstadoEmpleado(UUID empleadoId);
}
