package org.grupof.administracionapp.services.Departamento;

import org.grupof.administracionapp.entity.registroEmpleado.Departamento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartamentoService {
    List<Departamento> getAllDepartamentos();
}
