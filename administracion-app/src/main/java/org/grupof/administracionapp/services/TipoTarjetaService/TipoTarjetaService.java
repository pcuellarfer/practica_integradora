package org.grupof.administracionapp.services.TipoTarjetaService;

import org.grupof.administracionapp.entity.TipoTarjeta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TipoTarjetaService {
    List<TipoTarjeta> getAllTiposTarjetas();
    TipoTarjeta getTipoTarjetaById(UUID id);
}
