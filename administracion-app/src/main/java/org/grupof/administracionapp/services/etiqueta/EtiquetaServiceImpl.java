package org.grupof.administracionapp.services.etiqueta;

import org.grupof.administracionapp.entity.Etiqueta;
import org.grupof.administracionapp.repository.EtiquetaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio para la gestión de etiquetas.
 * Proporciona operaciones para guardar etiquetas, buscar etiquetas por distintos criterios
 * y obtener etiquetas individuales.
 */
@Service
public class EtiquetaServiceImpl implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    /**
     * Constructor que inyecta el repositorio de etiquetas.
     *
     * @param etiquetaRepository repositorio para acceso a datos de etiquetas.
     */
    public EtiquetaServiceImpl(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    /**
     * Guarda una etiqueta en la base de datos.
     * <p>
     * Si la etiqueta no tiene ID (es nueva), verifica que no exista ya una etiqueta
     * con el mismo jefe y texto para evitar duplicados, lanzando una excepción en tal caso.
     * </p>
     *
     * @param etiqueta la entidad Etiqueta a guardar.
     * @throws IllegalArgumentException si ya existe una etiqueta con el mismo jefe y texto.
     */
    @Override
    public void guardarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta.getId() == null) {
            if (etiquetaRepository.existsByJefeAndTexto(etiqueta.getJefe(), etiqueta.getTexto())) {
                throw new IllegalArgumentException("Esta etiqueta ya existe para este jefe");
            }
        }
        etiquetaRepository.save(etiqueta);
    }

    /**
     * Busca y devuelve una lista de etiquetas cuyo ID esté en la lista proporcionada.
     *
     * @param ids lista de UUIDs de etiquetas a buscar.
     * @return lista de etiquetas encontradas.
     */
    @Override
    public List<Etiqueta> buscarPorIds(List<UUID> ids) {
        return etiquetaRepository.findAllById(ids);
    }

    /**
     * Busca etiquetas asociadas a un empleado determinado por su ID.
     *
     * @param empleadoId UUID del empleado para el cual se buscan etiquetas.
     * @return lista de etiquetas asociadas al empleado.
     */
    @Override
    public List<Etiqueta> buscarPorEmpleadoId(UUID empleadoId) {
        return etiquetaRepository.findByEmpleadosEtiquetados_Id(empleadoId);
    }

    /**
     * Busca una etiqueta por su ID.
     *
     * @param id UUID de la etiqueta a buscar.
     * @return un Optional con la etiqueta encontrada, o vacío si no existe.
     */
    @Override
    public Optional<Etiqueta> buscarPorId(UUID id) {
        return etiquetaRepository.findById(id);
    }
}

