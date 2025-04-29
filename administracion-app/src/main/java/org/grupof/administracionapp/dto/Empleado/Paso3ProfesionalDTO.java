package org.grupof.administracionapp.dto.Empleado;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter

public class Paso3ProfesionalDTO {
    private UUID departamento;
    private Set<UUID> especialidades;
}
