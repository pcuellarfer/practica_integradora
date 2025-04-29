package org.grupof.administracionapp.services.Genero;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public abstract class GeneroService {
    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public String obtenerNombreGeneroPorId(UUID id) {
        return generoRepository.findById(id)
                .map(Genero::getIdentidad)
                .orElse("Género desconocido");
    }

    public abstract List<Genero> getAllGeneros();
}

