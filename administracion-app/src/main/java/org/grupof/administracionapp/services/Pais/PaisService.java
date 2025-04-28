package org.grupof.administracionapp.services.Pais;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaisService {
     List<Pais> getAllPaises();
}
