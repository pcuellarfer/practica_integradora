package org.grupof.administracionapp.services.TipoDocumento;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TipoDocumentoService {
    List<TipoDocumento> getAllTipoDocumento();
    TipoDocumento getTipoDocumentoById(UUID id);
}
