package org.grupof.administracionapp.mapper;

import org.grupof.administracionapp.dto.Empleado.EmpleadoDTO;
import org.grupof.administracionapp.entity.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {

    @Mapping(source = "id", target = "id")
    EmpleadoDTO toDTO(Empleado empleado);
}

