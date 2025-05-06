package org.grupof.administracionapp.dto.Empleado;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class RegistroEmpleadoDTO {

    //uuid y la foto del empleado
    private UUID empleadoId;
    private String fotoUrl;

    private Paso1PersonalDTO paso1PersonalDTO;
    private Paso2ContactoDTO paso2ContactoDTO;
    private Paso3ProfesionalDTO paso3ProfesionalDTO;
    private Paso4EconomicosDTO paso4EconomicosDTO;

}