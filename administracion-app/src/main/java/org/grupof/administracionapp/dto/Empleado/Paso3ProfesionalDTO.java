package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter

public class Paso3ProfesionalDTO {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
//    Si se recibe un código de departamento que
//    no se encuentra entre los que aparecen en la
//    tabla de departamentos, se producirá un Error
//    de la aplicación
    private UUID departamento;
//    Se deben seleccionar al menos 2
//    especialidades.
//- Si se recibe un código de especialidad que no
//    se encuentra entre los que aparecen en la tabla
//    de especialidades, se producirá un Error de la
//    aplicación.
    @NotEmpty(message = "Este campo es obligatorio y no puede estar vacío")
    //al devolver un set, hay que comprobar que el set no este vacio, en vez de null
    private Set<UUID> especialidades;
}
