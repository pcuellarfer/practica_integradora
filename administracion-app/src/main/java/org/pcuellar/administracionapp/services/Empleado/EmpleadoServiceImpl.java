package org.pcuellar.administracionapp.services.Empleado;

import org.pcuellar.administracionapp.dto.Empleado.EmpleadoDTO;
import org.pcuellar.administracionapp.repository.EmpleadoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link EmpleadoService} que gestiona
 * las operaciones CRUD relacionadas con los empleados.
 * Utiliza {@link EmpleadoRepository} para interactuar con la base de datos.
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    /**
     * Constructor que inyecta el repositorio de empleados.
     *
     * @param empleadoRepository repositorio para acceso a datos de empleados.
     */
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Registra un nuevo empleado en la base de datos.
     * No retorna el objeto persistido ya que es un método void.
     *
     * @param empleadoDTO datos del empleado a registrar.
     */
    @Override
    public void registrarEmpleado(EmpleadoDTO empleadoDTO) {
        org.pcuellar.administracionapp.entity.Empleado nuevoEmpleado = new org.pcuellar.administracionapp.entity.Empleado();
        BeanUtils.copyProperties(empleadoDTO, nuevoEmpleado);
        org.pcuellar.administracionapp.entity.Empleado guardado = empleadoRepository.save(nuevoEmpleado);

        // Copia de datos al DTO (no se utiliza el resultado en este método)
        EmpleadoDTO resultado = new EmpleadoDTO();
        BeanUtils.copyProperties(guardado, resultado);
    }

    /**
     * Edita los datos de un empleado existente.
     *
     * @param id identificador del empleado a editar.
     * @param dto objeto con los nuevos datos del empleado.
     * @return DTO con los datos actualizados, o null si no se encontró el empleado.
     */
    @Override
    public EmpleadoDTO editarEmpleado(UUID id, EmpleadoDTO dto) {
        Optional<org.pcuellar.administracionapp.entity.Empleado> opt = empleadoRepository.findById(id);
        if (opt.isEmpty()) return null;

        org.pcuellar.administracionapp.entity.Empleado empleado = opt.get();
        BeanUtils.copyProperties(dto, empleado);
        org.pcuellar.administracionapp.entity.Empleado actualizado = empleadoRepository.save(empleado);

        EmpleadoDTO resultado = new EmpleadoDTO();
        BeanUtils.copyProperties(actualizado, resultado);
        return resultado;
    }

    /**
     * Elimina un empleado por su identificador.
     *
     * @param id identificador del empleado a eliminar.
     * @return true si el empleado fue eliminado, false si no existía.
     */
    @Override
    public boolean eliminarEmpleado(UUID id) {
        if (!empleadoRepository.existsById(id)) return false;
        empleadoRepository.deleteById(id);
        return true;
    }

    /**
     * Busca un empleado por su identificador.
     *
     * @param id identificador del empleado.
     * @return DTO del empleado encontrado o null si no existe.
     */
    @Override
    public EmpleadoDTO buscarEmpleado(UUID id) {
        return empleadoRepository.findById(id)
                .map(emp -> {
                    EmpleadoDTO dto = new EmpleadoDTO();
                    BeanUtils.copyProperties(emp, dto);
                    return dto;
                }).orElse(null);
    }

    /**
     * Obtiene una lista de todos los empleados registrados.
     *
     * @return lista de DTOs de empleados.
     */
    @Override
    public List<EmpleadoDTO> listarEmpleados() {
        return empleadoRepository.findAll().stream().map(emp -> {
            EmpleadoDTO dto = new EmpleadoDTO();
            BeanUtils.copyProperties(emp, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
