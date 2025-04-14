package org.pcuellar.administracionapp.services;

import org.pcuellar.administracionapp.dto.SolicitudColaboracionDTO;

import java.util.List;
import java.util.UUID;

public interface SolicitudColaboracionService {
    void solicitarColaboracion(UUID usuarioId, UUID receptorId);
    void aceptarColaboracion(UUID id);
    void rechazarColaboracion(UUID id);
    List<SolicitudColaboracionDTO> obtenerSolicitudesPendientes(UUID usuarioId);
    List<SolicitudColaboracionDTO> obtenerSolicitudesRecibidas(UUID usuarioId);
    List<SolicitudColaboracionDTO> obtenerSolicitudesEnviadas(UUID usuarioId);
}
