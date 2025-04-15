package org.pcuellar.administracionapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor  @Data
public class RegistroUsuarioDTO {
    private String nombre;
    private String contrasena;
    private String email;
}
