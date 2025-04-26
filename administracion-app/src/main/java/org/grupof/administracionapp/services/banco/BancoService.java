package org.grupof.administracionapp.services.banco;

import org.grupof.administracionapp.entity.Banco;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BancoService {
    List<Banco> getAllBancos();
}
