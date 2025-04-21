package org.pcuellar.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroEmpleadoDTO {
    //faltan mas validaciones o tipos de campo que no se especifican por cambio de enunciado

    //parte personal
    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String Localidad;

    //parte empresarial
    @NotBlank
    private String departamento;

    @NotBlank
    private String puesto;
}