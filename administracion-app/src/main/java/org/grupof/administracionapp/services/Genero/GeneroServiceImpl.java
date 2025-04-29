package org.grupof.administracionapp.services.Genero;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.grupof.administracionapp.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroServiceImpl extends GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroServiceImpl(GeneroRepository generoRepository) {
        super(generoRepository);
        this.generoRepository = generoRepository;
    }


    @Override
    public List<Genero> getAllGeneros() {
        return generoRepository.findAll();
    }
}
