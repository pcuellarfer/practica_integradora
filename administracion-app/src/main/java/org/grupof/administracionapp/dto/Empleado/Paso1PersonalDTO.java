package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Paso1PersonalDTO {

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String nombre;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String apellido;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    imagen debe ser obligatoriamente gif o jpg
//    peso maximo 200kb
    private MultipartFile foto;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe unas siglas de género que no se
//    encuentren entre las que aparecen en el
//    dominio de géneros
    private UUID genero;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    //mas de 18 años
    private LocalDate fechaNacimiento;

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Debe coincidir con el número de años
//    trascurridos desde la fecha de nacimiento.
    private Integer edad;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe un código de país que no se
//    encuentra entre los que aparecen en la tabla
//    de países, se producirá un Error de la aplicación
    private UUID pais;

    @NotBlank(message = "Este campo es obligatorio y no puede estar vacío")
    private String comentarios;
}
