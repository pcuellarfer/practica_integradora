package org.grupof.administracionapp.services.etiqueta;

import org.grupof.administracionapp.entity.Etiqueta;
import org.grupof.administracionapp.repository.EtiquetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EtiquetaServiceImpl implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaServiceImpl(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @Override
    public void guardarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta.getId() == null) {
            if (etiquetaRepository.existsByJefeAndTexto(etiqueta.getJefe(), etiqueta.getTexto())) {
                throw new IllegalArgumentException("esta etiqueta ya existe para este jefe");
            }
        }
        etiquetaRepository.save(etiqueta);
    }

    @Override
    public List<Etiqueta> buscarPorIds(List<UUID> ids) {
        return etiquetaRepository.findAllById(ids);
    }

    @Override
    public List<Etiqueta> buscarPorEmpleadoId(UUID empleadoId) {
        return etiquetaRepository.findByEmpleadosEtiquetados_Id(empleadoId);
    }

    @Override
    public Optional<Etiqueta> buscarPorId(UUID id) {
        return etiquetaRepository.findById(id);
    }
}
