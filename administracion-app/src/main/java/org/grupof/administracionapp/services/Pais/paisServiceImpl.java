package org.grupof.administracionapp.services.Pais;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.grupof.administracionapp.repository.PaisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class paisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;

    public paisServiceImpl(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public List<Pais> getAllPaises() {
        return paisRepository.findAll();
    }

    @Override
    public Pais getPaisById(UUID id) {
        return paisRepository.findById(id).orElse(null);
    }

    @Override
    public String obtenerNombrePais(UUID id) {
        return paisRepository.findById(id)
                .map(Pais::getNombre)
                .orElse("Desconocido");
    }
}
