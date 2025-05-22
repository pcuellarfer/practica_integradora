package org.grupof.administracionapp.services.banco;

import org.grupof.administracionapp.entity.registroEmpleado.Banco;

import java.util.List;
import java.util.UUID;

public interface BancoService {
    List<Banco> getAllBancos();
    Banco GetBancoById(UUID id);
}
