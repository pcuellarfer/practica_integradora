package org.pcuellar.administracionapp.services.Empleado;

import org.pcuellar.administracionapp.Model.Empleado;
import org.pcuellar.administracionapp.dto.Empleado.EmpleadoDTO;
import org.pcuellar.administracionapp.repository.EmpleadoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public EmpleadoDTO registrarEmpleado(Empleado empleado) {
        org.pcuellar.administracionapp.entity.Empleado nuevoEmpleado = new org.pcuellar.administracionapp.entity.Empleado();
        BeanUtils.copyProperties(empleado, nuevoEmpleado);
        org.pcuellar.administracionapp.entity.Empleado guardado = empleadoRepository.save(nuevoEmpleado);

        EmpleadoDTO resultado = new EmpleadoDTO();
        BeanUtils.copyProperties(guardado, resultado);
        return resultado;
    }

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

    @Override
    public boolean eliminarEmpleado(UUID id) {
        if (!empleadoRepository.existsById(id)) return false;
        empleadoRepository.deleteById(id);
        return true;
    }

    @Override
    public EmpleadoDTO buscarEmpleado(UUID id) {
        return empleadoRepository.findById(id)
                .map(emp -> {
                    EmpleadoDTO dto = new EmpleadoDTO();
                    BeanUtils.copyProperties(emp, dto);
                    return dto;
                }).orElse(null);
    }

    @Override
    public List<EmpleadoDTO> listarEmpleados() {
        return empleadoRepository.findAll().stream().map(emp -> {
            EmpleadoDTO dto = new EmpleadoDTO();
            BeanUtils.copyProperties(emp, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
