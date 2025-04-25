package org.grupof.administracionapp.services.TipoDocumento;

import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;

import org.grupof.administracionapp.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoImpl implements TipoDocumentoService {

    private final TipoDocumentoRepository TipoDocumentoRepository;

    public TipoDocumentoImpl(org.grupof.administracionapp.repository.TipoDocumentoRepository tipoDocumentoRepository) {
        TipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Override
    public List<TipoDocumento> getAllTipoDocumento() {
        return TipoDocumentoRepository.findAll();
    }
}
