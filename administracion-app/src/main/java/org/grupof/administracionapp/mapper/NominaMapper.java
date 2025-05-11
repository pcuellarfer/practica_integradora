package org.grupof.administracionapp.mapper;

import org.grupof.administracionapp.dto.Nomina.LineaNominaDTO;
import org.grupof.administracionapp.dto.Nomina.NominaDTO;
import org.grupof.administracionapp.entity.LineaNomina;
import org.grupof.administracionapp.entity.Nomina;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NominaMapper {

    @Mapping(source = "empleado.id", target = "empleadoId")
    NominaDTO toDTO(Nomina nomina);

    @Mapping(target = "empleado", ignore = true) // La asociación con el empleado se maneja en el servicio
    @Mapping(target = "lineas", ignore = true) // Las líneas se mapean por separado
    Nomina toEntity(NominaDTO nominaDTO);

    LineaNominaDTO toLineaNominaDTO(LineaNomina lineaNomina);

    LineaNomina toLineaNominaEntity(LineaNominaDTO lineaNominaDTO);

    List<LineaNominaDTO> toLineaNominaDTOList(List<LineaNomina> lineasNomina);

    List<LineaNomina> toLineaNominaEntityList(List<LineaNominaDTO> lineaNominaDTOList);
}