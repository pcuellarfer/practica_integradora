package org.grupof.administracionapp.services.Departamento;

import org.grupof.administracionapp.entity.registroEmpleado.Departamento;
import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.grupof.administracionapp.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio {@link DepartamentoService} que proporciona
 * operaciones para consultar y recuperar información sobre los departamentos.
 */
@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    /**
     * Constructor que inyecta el repositorio de departamentos.
     *
     * @param departamentoRepository el repositorio que gestiona la persistencia de los departamentos
     */
    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    /**
     * Recupera un departamento a partir de su identificador único.
     *
     * @param id el UUID del departamento que se desea buscar
     * @return el objeto {@link Departamento} si se encuentra, o {@code null} si no existe
     */
    @Override
    public List<Departamento> getAllDepartamentos() {
        return departamentoRepository.findAll();
    }

    /**
     * Obtiene un departamento por su identificador único.
     *
     * @param id el UUID del departamento a buscar
     * @return el objeto {@link Departamento} correspondiente al ID, o {@code null} si no se encuentra
     */
    @Override
    public Departamento getDepartamentoById(UUID id) {
        return departamentoRepository.findById(id).orElse(null);
    }

    /**
     * Obtiene el nombre del departamento dado su identificador único.
     *
     * @param idDepartamento el UUID del departamento
     * @return el nombre del departamento si se encuentra, o "Desconocido" si no existe
     */
    @Override
    public String obtenerNombreDepartamento(UUID idDepartamento) {
        return departamentoRepository.findById(idDepartamento)
                .map(Departamento::getNombre)
                .orElse("Desconocido");
    }
}
