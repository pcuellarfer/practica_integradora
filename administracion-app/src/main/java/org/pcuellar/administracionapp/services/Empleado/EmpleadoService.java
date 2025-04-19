package org.pcuellar.administracionapp.services.Empleado;

import java.util.UUID;

import org.pcuellar.administracionapp.dto.Empleado.EmpleadoDTO;
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
     * @param empleadoDTO Objeto con los datos del empleado a registrar.
     */
    void registrarEmpleado(EmpleadoDTO empleadoDTO);

    /**
     * Edita los datos de un empleado existente.
     *
     * @param id Identificador único del empleado a editar.
     * @param dto Objeto con los nuevos datos del empleado.
     * @return El empleado actualizado.
     */
    EmpleadoDTO editarEmpleado(UUID id, EmpleadoDTO dto);

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
    EmpleadoDTO buscarEmpleado(UUID id);

    /**
     * Lista todos los empleados registrados en el sistema.
     *
     * @return Una lista con todos los empleados.
     */
    List<EmpleadoDTO> listarEmpleados();
}
