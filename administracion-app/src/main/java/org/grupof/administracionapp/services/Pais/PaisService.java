package org.grupof.administracionapp.services.Pais;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PaisService {
     List<Pais> getAllPaises();
     Pais getPaisById(UUID id);
}
