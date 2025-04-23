package org.grupof.administracionapp.dto;

import org.grupof.administracionapp.auxiliar.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class SolicitudColaboracionDTO {
    private UUID id;
    private Status status;
    private LocalDateTime fechaSolicitud;
}
