package org.grupof.administracionapp.services.TipoVia;

import org.grupof.administracionapp.entity.registroEmpleado.TipoVia;
import org.grupof.administracionapp.repository.TipoViaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio {@link TipoViaService} que gestiona
 * las operaciones relacionadas con la entidad {@link TipoVia}.
 * Proporciona métodos para obtener todos los tipos de vía y buscar
 * un tipo de vía por su identificador único.
 */
@Service
public class TipoViaServiceImpl implements TipoViaService{

    private final TipoViaRepository tipoViaRepository;

    /**
     * Constructor que inyecta el repositorio {@link TipoViaRepository}.
     *
     * @param tipoViaRepository Repositorio para acceder a datos de tipos de vía.
     */
    public TipoViaServiceImpl(TipoViaRepository tipoViaRepository) {
        this.tipoViaRepository = tipoViaRepository;
    }

    /**
     * Obtiene la lista completa de tipos de vía almacenados en la base de datos.
     *
     * @return Lista con todos los tipos de vía.
     */
    @Override
    public List<TipoVia> getAllTipoVia() {
        return tipoViaRepository.findAll();
    }

    /**
     * Busca un tipo de vía por su identificador único.
     *
     * @param id Identificador UUID del tipo de vía.
     * @return Objeto {@link TipoVia} si existe, o {@code null} si no se encuentra.
     */
    @Override
    public TipoVia getTipoViaById(UUID id) {
        return tipoViaRepository.findById(id).orElse(null);
    }
}
