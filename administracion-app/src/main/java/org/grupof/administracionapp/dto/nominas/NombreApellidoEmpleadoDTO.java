package org.grupof.administracionapp.dto.nominas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data

public class NombreApellidoEmpleadoDTO {
    private UUID id;
    private String nombre;
    private String apellido;
}
