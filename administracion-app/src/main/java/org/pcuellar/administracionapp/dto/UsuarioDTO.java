package org.pcuellar.administracionapp.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public class UsuarioDTO {
    private UUID id;
    private String nombre;
    //nunca se deveria devolver la contraseña
    //private String contrasena;
    private String email;
    private boolean estadoBloqueado = false;
    private int intentosFallidos = 0;
    private LocalDateTime bloqueoFechaHora;
    private String motivoBloqueo;
}
