package org.grupof.administracionapp.services.TipoTarjetaService;

import org.grupof.administracionapp.entity.TipoTarjeta;
import org.grupof.administracionapp.repository.TipoTarjetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio {@link TipoTarjetaService} que gestiona
 * las operaciones relacionadas con la entidad {@link TipoTarjeta}.
 * Proporciona métodos para obtener todos los tipos de tarjeta y buscar
 * un tipo de tarjeta por su identificador único.
 */
@Service
public class TipoTarjetaServiceImpl implements TipoTarjetaService {

    private final TipoTarjetaRepository tipoTarjetaRepository;

    /**
     * Constructor que inyecta el repositorio {@link TipoTarjetaRepository}.
     *
     * @param tipoTarjetaRepository Repositorio para acceder a datos de tipos de tarjeta.
     */
    public TipoTarjetaServiceImpl(TipoTarjetaRepository tipoTarjetaRepository) {
        this.tipoTarjetaRepository = tipoTarjetaRepository;
    }

    /**
     * Obtiene la lista completa de tipos de tarjeta almacenados en la base de datos.
     *
     * @return Lista con todos los tipos de tarjeta.
     */
    @Override
    public List<TipoTarjeta> getAllTiposTarjetas() {
        return tipoTarjetaRepository.findAll();
    }

    /**
     * Busca un tipo de tarjeta por su identificador único.
     *
     * @param id Identificador UUID del tipo de tarjeta.
     * @return Objeto {@link TipoTarjeta} si existe, o {@code null} si no se encuentra.
     */
    @Override
    public TipoTarjeta getTipoTarjetaById(UUID id) {
        return tipoTarjetaRepository.findById(id).orElse(null);
    }
}
