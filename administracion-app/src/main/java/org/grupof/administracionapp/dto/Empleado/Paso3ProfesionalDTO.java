package org.grupof.administracionapp.dto.Empleado;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.grupof.administracionapp.validations.paso3DTO.departamentoValido.DepartamentoValido;
import org.grupof.administracionapp.validations.paso3DTO.especialidadesValidas.EspecialidadesValidas;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter

public class Paso3ProfesionalDTO {

    @NotNull(message = "Este campo es obligatorio y no puede estar vacío")
    @DepartamentoValido
    private UUID departamento;


    @EspecialidadesValidas
    @Size(min = 2, message = "Debes seleccionar al menos dos especialidades")
    @NotEmpty(message = "Este campo es obligatorio y no puede estar vacío")
    private Set<UUID> especialidades;
}
