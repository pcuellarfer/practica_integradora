package org.grupof.administracionapp.services.etiqueta;

import org.grupof.administracionapp.entity.Etiqueta;
import org.springframework.stereotype.Service;

@Service
public interface EtiquetaService {
    void guardarEtiqueta(Etiqueta etiqueta);
}
