package org.pcuellar.administracionapp.services;

import org.pcuellar.administracionapp.dto.MensajeDTO;

import java.util.List;
import java.util.UUID;

public interface MensajeService {
    void enviarMensaje(UUID emisorId, UUID destinatarioId, String mensaje);
    List<MensajeDTO> obtenerMensajes(UUID usuarioId);
    List<MensajeDTO> obtenerMensajesEntreUsuarios(UUID emisorId, UUID receptorId);
}
