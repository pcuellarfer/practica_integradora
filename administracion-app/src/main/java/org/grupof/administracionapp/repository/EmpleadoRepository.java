package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de acceso a datos para la entidad {@link Empleado}.
 * Proporciona métodos personalizados de consulta, además de los heredados de {@link JpaRepository}.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {

    /**
     * Busca un empleado por el ID del usuario asociado.
     *
     * @param usuarioId ID del usuario.
     * @return un {@link Optional} que contiene el empleado si existe.
     */
    Optional<Empleado> findByUsuarioId(UUID usuarioId);

    /**
     * Busca empleados cuyo nombre contenga una cadena específica, sin importar mayúsculas/minúsculas.
     *
     * @param nombre cadena parcial del nombre a buscar.
     * @return lista de empleados que contienen esa cadena en su nombre.
     */
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca empleados por el ID del género.
     *
     * @param generoId ID del género a buscar.
     * @return lista de empleados que tienen ese género.
     */
    List<Empleado> findByGeneroId(UUID generoId);

    /**
     * Busca empleados cuyo nombre contenga una cadena específica (ignorando mayúsculas/minúsculas)
     * y que pertenezcan a un género específico.
     *
     * @param nombre   cadena parcial del nombre.
     * @param generoId ID del género.
     * @return lista de empleados que cumplen ambas condiciones.
     */
    List<Empleado> findByNombreContainingIgnoreCaseAndGeneroId(String nombre, UUID generoId);

    /**
     * Busca todos los empleados excepto el que tiene el ID especificado.
     *
     * @param id ID del empleado a excluir.
     * @return lista de empleados excluyendo al empleado con ese ID.
     */
    List<Empleado> findByIdNot(UUID id);

    List<Empleado> findByJefe(Empleado jefe);
}


