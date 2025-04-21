package org.pcuellar.administracionapp.services.Empleado;

import org.modelmapper.ModelMapper;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.dto.Usuario.UsuarioDTO;
import org.pcuellar.administracionapp.entity.Empleado;
import org.pcuellar.administracionapp.entity.Usuario;
import org.pcuellar.administracionapp.repository.EmpleadoRepository;
import org.pcuellar.administracionapp.repository.UsuarioRepository;
import org.pcuellar.administracionapp.services.Usuario.UsuarioService;
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


    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, UsuarioService usuarioService, ModelMapper modelMapper) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO, UsuarioDTO usuarioDTO) {
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
        Optional<org.pcuellar.administracionapp.entity.Empleado> opt = empleadoRepository.findById(id);
        if (opt.isEmpty()) return null;

        org.pcuellar.administracionapp.entity.Empleado empleado = opt.get();
        BeanUtils.copyProperties(dto, empleado);
        org.pcuellar.administracionapp.entity.Empleado actualizado = empleadoRepository.save(empleado);

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
