package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.validations.paso1DTO.fechaNaz.MayorDeEdad;
import org.grupof.administracionapp.validations.paso1DTO.generoValido.GeneroValido;
import org.grupof.administracionapp.validations.paso1DTO.paisValido.PaisValido;
import org.springframework.format.annotation.DateTimeFormat;
import org.grupof.administracionapp.validations.paso1DTO.edadIgualAFecha.EdadConsistente;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter

@EdadConsistente //validacion para comparar la fecha de nacimiento con la edad
public class Paso1PersonalDTO {

    @NotBlank(message = "el campo nombre es obligatorio y no puede estar vacío")
    private String nombre;

    @NotBlank(message = "el campo apellido es obligatorio y no puede estar vacío")
    private String apellido;

    @NotNull(message = "se tiene que seleccionar un genero si o si")
    @GeneroValido
    private UUID genero;

    @NotNull(message = "el campo fecha de nacimiento es obligatorio y no puede estar vacío")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @MayorDeEdad
    private LocalDate fechaNacimiento;

    @NotNull(message = "tienes que meter una fecha si o si")
    //validado a nivel de clase que coincidan con la fecha nacimiento
    private Integer edad;

    @NotNull(message = "selecciona el un pais, por favor te lo pido")
    @PaisValido
    private UUID pais;

    @NotBlank(message = "Este campo es obligatorio")
    private String comentarios;
}
