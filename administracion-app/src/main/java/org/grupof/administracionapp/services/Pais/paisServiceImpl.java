package org.grupof.administracionapp.services.Pais;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.grupof.administracionapp.repository.PaisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
