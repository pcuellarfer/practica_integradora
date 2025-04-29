package org.grupof.administracionapp.services.Especialidades;

import org.grupof.administracionapp.entity.registroEmpleado.Especialidad;
import org.grupof.administracionapp.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadesServiceImpl implements EspecialidadesService{

    private final EspecialidadRepository especialidadesRepository;

    public EspecialidadesServiceImpl(EspecialidadRepository especialidadesRepository) {
        this.especialidadesRepository = especialidadesRepository;
    }


    @Override
    public List<Especialidad> getAllEspecialidades() {
        return especialidadesRepository.findAll();
    }
}
