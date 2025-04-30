package org.grupof.administracionapp.services.TipoVia;

import org.grupof.administracionapp.entity.registroEmpleado.TipoVia;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TipoViaService {

    List<TipoVia> getAllTipoVia();
    TipoVia getTipoViaById(UUID id);
}
