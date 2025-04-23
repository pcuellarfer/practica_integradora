package org.grupof.administracionapp.dto.Usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor  @Data
public class RegistroUsuarioDTO {

    //faltan validaciones para los campos a registrar

    @NotBlank
    private String email;

    @NotBlank
    private String contrasena;

}
