package org.grupof.administracionapp.services.banco;

import org.grupof.administracionapp.entity.Banco;
import org.grupof.administracionapp.repository.BancoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
