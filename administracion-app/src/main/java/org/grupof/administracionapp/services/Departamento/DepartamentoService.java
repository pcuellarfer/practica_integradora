package org.grupof.administracionapp.services.Departamento;

import org.grupof.administracionapp.entity.registroEmpleado.Departamento;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DepartamentoService {
    List<Departamento> getAllDepartamentos();
    Departamento getDepartamentoById(UUID id);
}
