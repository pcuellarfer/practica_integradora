package org.grupof.administracionapp.services.TipoDocumento;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TipoDocumentoService {
    List<TipoDocumento> getAllTipoDocumento();
}
