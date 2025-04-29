package org.grupof.administracionapp.services.TipoVia;

import org.grupof.administracionapp.entity.registroEmpleado.TipoVia;
import org.grupof.administracionapp.repository.TipoViaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TipoViaServiceImpl implements TipoViaService{

    private final  TipoViaRepository tipoViaRepository;

    public TipoViaServiceImpl(TipoViaRepository tipoViaRepository) {
        this.tipoViaRepository = tipoViaRepository;
    }

    @Override
    public List<TipoVia> getAllTipoVia() {
        return tipoViaRepository.findAll();
    }

    @Override
    public TipoVia getTipoViaById(UUID id) {
        return tipoViaRepository.findById(id).orElse(null);
    }
}
