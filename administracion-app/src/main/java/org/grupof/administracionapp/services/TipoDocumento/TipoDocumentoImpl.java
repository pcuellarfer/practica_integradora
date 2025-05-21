package org.grupof.administracionapp.services.TipoDocumento;

import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;
import org.grupof.administracionapp.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio {@link TipoDocumentoService} que gestiona las operaciones
 * relacionadas con los tipos de documento.
 */
@Service
public class TipoDocumentoImpl implements TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    /**
     * Constructor para inyectar el repositorio de tipo de documento.
     *
     * @param tipoDocumentoRepository repositorio para acceso a datos de tipo de documento.
     */
    public TipoDocumentoImpl(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    /**
     * Obtiene todos los tipos de documento registrados en la base de datos.
     *
     * @return una lista de entidades {@link TipoDocumento}.
     */
    @Override
    public List<TipoDocumento> getAllTipoDocumento() {
        return tipoDocumentoRepository.findAll();
    }

    /**
     * Obtiene un tipo de documento por su identificador único.
     *
     * @param id el UUID del tipo de documento a buscar.
     * @return la entidad {@link TipoDocumento} si se encuentra, o {@code null} en caso contrario.
     */
    @Override
    public TipoDocumento getTipoDocumentoById(UUID id) {
        return tipoDocumentoRepository.findById(id).orElse(null);
    }
}