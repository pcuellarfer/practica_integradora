package org.grupof.administracionapp.services.banco;

import org.grupof.administracionapp.entity.registroEmpleado.Banco;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BancoService {
    List<Banco> getAllBancos();
    Banco GetBancoById(UUID id);
}
