package org.grupof.administracionapp.services.Genero;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GeneroService {
    List<Genero> getAllGeneros();
}
