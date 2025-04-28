package org.grupof.administracionapp.services.Departamento;

import org.grupof.administracionapp.entity.registroEmpleado.Departamento;
import org.grupof.administracionapp.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }


    @Override
    public List<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }
}
