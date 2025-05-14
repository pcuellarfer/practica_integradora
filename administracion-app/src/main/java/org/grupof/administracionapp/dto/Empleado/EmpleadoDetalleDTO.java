package org.grupof.administracionapp.dto.Empleado;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmpleadoDetalleDTO {
    private RegistroEmpleadoDTO empleado;
    private String nombreGenero;
    private String nombrePais;
    private String nombreDepartamento;
    private String nombreFoto;
    private String nombreJefe;
}
