package org.pcuellar.administracionapp.dto;

import java.util.UUID;
import java.time.LocalDateTime;

public class MensajeDTO {
    private UUID id;
    private String contenido;
    private LocalDateTime fechaEnvio;
}
