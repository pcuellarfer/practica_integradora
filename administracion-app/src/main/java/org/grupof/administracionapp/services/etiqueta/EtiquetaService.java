package org.grupof.administracionapp.services.etiqueta;

import org.grupof.administracionapp.entity.Etiqueta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface EtiquetaService {
    void guardarEtiqueta(Etiqueta etiqueta);
    List<Etiqueta> buscarPorIds(List<UUID> ids);
    List<Etiqueta> buscarPorEmpleadoId(UUID empleadoId);
    Optional<Etiqueta> buscarPorId(UUID id);
}
