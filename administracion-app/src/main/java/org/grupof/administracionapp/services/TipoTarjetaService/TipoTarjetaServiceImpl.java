package org.grupof.administracionapp.services.TipoTarjetaService;

import org.grupof.administracionapp.entity.TipoTarjeta;
import org.grupof.administracionapp.repository.TipoTarjetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoTarjetaServiceImpl implements TipoTarjetaService {

    private final TipoTarjetaRepository tipoTarjetaRepository;

    public TipoTarjetaServiceImpl(TipoTarjetaRepository tipoTarjetaRepository) {
        this.tipoTarjetaRepository = tipoTarjetaRepository;
    }

    @Override
    public List<TipoTarjeta> getAllTiposTarjetas() {
        return tipoTarjetaRepository.findAll();
    }
}
