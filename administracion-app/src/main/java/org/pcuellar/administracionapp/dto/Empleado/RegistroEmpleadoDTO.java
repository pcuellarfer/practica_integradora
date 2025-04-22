package org.pcuellar.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter


public class RegistroEmpleadoDTO {
    //faltan mas validaciones o tipos de campo que no se especifican por cambio de enunciado

    //parte personal
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String genero;


    //parte empresarial
    private String departamento;
    private String puesto;
}