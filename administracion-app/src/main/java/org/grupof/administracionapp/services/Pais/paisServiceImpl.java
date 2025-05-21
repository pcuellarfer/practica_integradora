package org.grupof.administracionapp.services.Pais;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.grupof.administracionapp.repository.PaisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio {@link PaisService} para gestionar
 * las operaciones relacionadas con la entidad {@link Pais}.
 * Proporciona métodos para obtener todos los países, buscar
 * un país por su ID y obtener el nombre de un país dado su ID.
 */
@Service
public class paisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;

    /**
     * Constructor que inyecta el repositorio {@link PaisRepository}.
     *
     * @param paisRepository Repositorio para acceder a datos de países.
     */
    public paisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    /**
     * Obtiene la lista completa de países almacenados en la base de datos.
     *
     * @return Lista con todos los países.
     */
    @Override
    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }

    /**
     * Busca un país por su identificador único.
     *
     * @param id Identificador UUID del país.
     * @return Objeto {@link Pais} si existe, o {@code null} si no se encuentra.
     */
    @Override
    public Pais getPaisById(UUID id) {
        return paisRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene el nombre de un país a partir de su identificador.
     *
     * @param id Identificador UUID del país.
     * @return Nombre del país, o la cadena "Desconocido" si no se encuentra.
     */
    @Override
    public String obtenerNombrePais(UUID id) {
        return paisRepository.findById(id)
                .map(Pais::getNombre)
                .orElse("Desconocido");
    }
}
