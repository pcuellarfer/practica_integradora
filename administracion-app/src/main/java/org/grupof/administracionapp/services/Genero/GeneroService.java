package org.grupof.administracionapp.services.Genero;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface GeneroService {

    List<Genero> getAllGeneros();
    Genero getGeneroById(UUID id);
}
