package org.pcuellar.administracionapp.services.Empleado;

import org.modelmapper.ModelMapper;
import org.pcuellar.administracionapp.dto.Empleado.EmpleadoDTO;
import org.pcuellar.administracionapp.dto.Empleado.RegistroEmpleadoDTO;
import org.pcuellar.administracionapp.entity.Empleado;
import org.pcuellar.administracionapp.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class EmpleadoServiceImpl implements EmpleadoService{

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public EmpleadoDTO registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO) {

        //devuelve empleado DTO con mucho NULL
        Empleado empleado = modelMapper.map(registroEmpleadoDTO, Empleado.class);
        //Empleado empleado = convertToEntity(registroEmpleadoDTO);
        empleado = empleadoRepository.save(empleado);

        return modelMapper.map(empleado, EmpleadoDTO.class);
    }

    @Override
    public EmpleadoDTO editarEmpleado(UUID id, EmpleadoDTO dto) {
        return null;
    }

    @Override
    public boolean eliminarEmpleado(UUID id) {
        return false;
    }

    @Override
    public EmpleadoDTO buscarEmpleado(UUID id) {
        return null;
    }

    @Override
    public List<EmpleadoDTO> listarEmpleados() {
        return List.of();
    }
}
