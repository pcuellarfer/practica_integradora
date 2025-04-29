package org.grupof.administracionapp.services.banco;

import org.grupof.administracionapp.entity.registroEmpleado.Banco;
import org.grupof.administracionapp.repository.BancoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BancoServiceImpl implements BancoService{

    private final BancoRepository bancoRepository;

    public BancoServiceImpl(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @Override
    public List<Banco> getAllBancos() {
        return bancoRepository.findAll();
    }

    @Override
    public Banco GetBancoById(UUID id) {
        return bancoRepository.findById(id).orElse(null);
    }


}
