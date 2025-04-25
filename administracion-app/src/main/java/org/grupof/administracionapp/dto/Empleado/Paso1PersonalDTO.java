package org.grupof.administracionapp.dto.Empleado;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Paso1PersonalDTO {
    private String nombre;
    private String apellido;
    private MultipartFile foto;
    private UUID genero;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private UUID pais;
    private String comentarios;
}
