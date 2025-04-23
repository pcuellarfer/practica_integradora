package org.grupof.administracionapp.services.Empleado;

import org.modelmapper.ModelMapper;
import org.grupof.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.grupof.administracionapp.dto.Usuario.UsuarioDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Usuario;
import org.grupof.administracionapp.repository.EmpleadoRepository;
import org.grupof.administracionapp.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    private final ModelMapper modelMapper = new ModelMapper();

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;


    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, UsuarioRepository usuarioRepository) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public RegistroEmpleadoDTO buscarEmpleadoPorUsuarioId(UUID usuarioId) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsuarioId(usuarioId);

        if (empleadoOpt.isPresent()) {
            RegistroEmpleadoDTO dto = new RegistroEmpleadoDTO();
            BeanUtils.copyProperties(empleadoOpt.get(), dto);
            return dto;
        }

        return null;
    }

    @Override
    public RegistroEmpleadoDTO registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO, UsuarioDTO usuarioDTO) {
        // Obtener el usuario de la base de datos
        Usuario usuario = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Mapear DTO a entidad
        Empleado empleado = modelMapper.map(registroEmpleadoDTO, Empleado.class);

        // Campos que no están en el DTO
        empleado.setFechaContratacion(LocalDateTime.now());
        empleado.setUsuario(usuario);

        // Guardar empleado
        empleadoRepository.save(empleado);
        return registroEmpleadoDTO;
    }

    /**
     * Edita los datos de un empleado existente.
     *
     * @param id identificador del empleado a editar.
     * @param dto objeto con los nuevos datos del empleado.
     * @return DTO con los datos actualizados, o null si no se encontró el empleado.
     */
    @Override
    public RegistroEmpleadoDTO editarEmpleado(UUID id, RegistroEmpleadoDTO dto) {
        Optional<Empleado> opt = empleadoRepository.findById(id);
        if (opt.isEmpty()) return null;

        Empleado empleado = opt.get();
        BeanUtils.copyProperties(dto, empleado);
        Empleado actualizado = empleadoRepository.save(empleado);

        RegistroEmpleadoDTO resultado = new RegistroEmpleadoDTO();
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
    public RegistroEmpleadoDTO buscarEmpleado(UUID id) {
        return empleadoRepository.findById(id)
                .map(emp -> {
                    RegistroEmpleadoDTO dto = new RegistroEmpleadoDTO();
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
    public List<RegistroEmpleadoDTO> listarEmpleados() {
        return empleadoRepository.findAll().stream().map(emp -> {
            RegistroEmpleadoDTO dto = new RegistroEmpleadoDTO();
            BeanUtils.copyProperties(emp, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
