package org.grupof.administracionapp.services.Categoria;

import org.grupof.administracionapp.entity.producto.Categoria;
import org.grupof.administracionapp.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementación del servicio {@link CategoriaService} para gestionar las operaciones relacionadas
 * con las categorías de productos.
 * Utiliza {@link CategoriaRepository} para acceder a los datos de categorías en la base de datos.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private static final Logger logger = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    /**
     * Constructor que inyecta el repositorio de categorías.
     *
     * @param categoriaRepository el repositorio para acceder a las categorías en la base de datos
     */
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * {@inheritDoc}
     * Obtiene todas las categorías de productos desde la base de datos.
     *
     * @return una lista de todas las categorías disponibles
     */
    @Override
    public List<Categoria> obtenerTodasLasCategorias() {
        logger.info("Obteniendo todas las categorías desde la base de datos");

        List<Categoria> categorias = categoriaRepository.findAll(Sort.by("nombre").ascending());
        if (categorias.isEmpty()) {
            logger.warn("No se encontraron categorías en la base de datos");
        } else {
            logger.info("Se encontraron {} categorías", categorias.size());
        }
        logger.debug("Número de categorías encontradas: {}", categorias.size());
        return categorias;
    }
}
