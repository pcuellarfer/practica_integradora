package org.pcuellar.administracionapp.dto.Empleado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistroEmpleadoDTO {
    private String nombre;
    private String contrasena;
    private String email;
}
