package org.grupof.administracionapp.services.etiqueta;

import org.grupof.administracionapp.entity.Etiqueta;
import org.grupof.administracionapp.repository.EtiquetaRepository;
import org.springframework.stereotype.Service;

@Service
public class EtiquetaServiceImpl implements EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaServiceImpl(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    @Override
    public void guardarEtiqueta(Etiqueta etiqueta) {
        if (etiquetaRepository.existsByJefeAndTexto(etiqueta.getJefe(), etiqueta.getTexto())) {
            throw new IllegalArgumentException("Etiqueta ya existe para este jefe");
        }
        etiquetaRepository.save(etiqueta);
    }
}
