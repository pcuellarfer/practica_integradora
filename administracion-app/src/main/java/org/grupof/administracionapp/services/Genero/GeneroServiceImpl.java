package org.grupof.administracionapp.services.Genero;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio {@link GeneroService} para la gestión de géneros.
 * Este servicio proporciona acceso a los datos de géneros mediante el repositorio {@link GeneroRepository}.
 */
@Service
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generoRepository;

    /**
     * Constructor que inyecta el repositorio de género.
     *
     * @param generoRepository el repositorio que gestiona las operaciones de persistencia para {@link Genero}
     */
    public GeneroServiceImpl(GeneroRepository generoRepository) {
        super();
        this.generoRepository = generoRepository;
    }

    /**
     * Recupera todos los géneros registrados en la base de datos.
     *
     * @return una lista de objetos {@link Genero}
     */
    @Override
    public List<Genero> getAllGeneros() {
        return generoRepository.findAll();
    }

    /**
     * Busca un género por su identificador único.
     *
     * @param id el UUID del género que se desea recuperar
     * @return el objeto {@link Genero} correspondiente al ID, o {@code null} si no se encuentra
     */
    @Override
    public Genero getGeneroById(UUID id) {
        return generoRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene el nombre del género a partir de su ID.
     *
     * @param idGenero el identificador UUID del género
     * @return una cadena con el nombre del género si se encuentra, o "Desconocido" si no existe
     */
    @Override
    public String obtenerNombreGenero(UUID idGenero) {
        return generoRepository.findById(idGenero)
                .map(Genero::getIdentidad)
                .orElse("Desconocido");
    }
}
