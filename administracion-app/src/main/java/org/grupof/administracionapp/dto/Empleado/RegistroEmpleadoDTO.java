package org.grupof.administracionapp.dto.Empleado;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class RegistroEmpleadoDTO {

    //para guardar temporalmente el archivo
    //no se puede hacer o no he conseguido hacer multipart porque no sobrevive entre redirecciones
    private byte[] fotoBytes;
    private String fotoTipo;

    //uuid y la foto del empleado
    private UUID empleadoId;
    private String fotoUrl;

    private Paso1PersonalDTO paso1PersonalDTO;
    private Paso2ContactoDTO paso2ContactoDTO;
    private Paso3ProfesionalDTO paso3ProfesionalDTO;
    private Paso4EconomicosDTO paso4EconomicosDTO;

}