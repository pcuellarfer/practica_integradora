package org.pcuellar.administracionapp.dto;

import org.pcuellar.administracionapp.auxiliar.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public class SolicitudColaboracionDTO {
    private UUID id;
    private Status status;
    private LocalDateTime fechaSolicitud;
}
