package org.grupof.administracionapp.dto;

import java.util.UUID;
import java.time.LocalDateTime;

//dto para un supuesto mensaje que nunca llego...
public class MensajeDTO {
    private UUID id;
    private String contenido;
    private LocalDateTime fechaEnvio;
}
