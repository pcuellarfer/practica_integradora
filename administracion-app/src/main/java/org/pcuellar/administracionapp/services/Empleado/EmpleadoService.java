package org.pcuellar.administracionapp.services.Empleado;

import java.util.UUID;

import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
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
     * Registra un nuevo empleado en el sistema.
     *
     * @param registroEmpleadoDTO Objeto con los datos del empleado a registrar.
     */
    void registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO, UsuarioDTO usuarioDTO);


    /**
     * Edita los datos de un empleado existente.
     *
     * @param id Identificador único del empleado a editar.
     * @param dto Objeto con los nuevos datos del empleado.
     * @return El empleado actualizado.
     */
    RegistroEmpleadoDTO editarEmpleado(UUID id, RegistroEmpleadoDTO dto);

    /**
     * Elimina lógicamente un empleado del sistema.
     *
     * @param id Identificador único del empleado a eliminar.
     * @return true si el empleado fue eliminado exitosamente, false en caso contrario.
     */
    boolean eliminarEmpleado(UUID id);

    /**
     * Busca y devuelve un empleado por su identificador.
     *
     * @param id Identificador único del empleado.
     * @return El empleado encontrado, o null si no existe.
     */
    RegistroEmpleadoDTO buscarEmpleado(UUID id);

    /**
     * Lista todos los empleados registrados en el sistema.
     *
     * @return Una lista con todos los empleados.
     */
    List<RegistroEmpleadoDTO> listarEmpleados();

    public RegistroEmpleadoDTO buscarEmpleadoPorUsuarioId(UUID usuarioId);
}
