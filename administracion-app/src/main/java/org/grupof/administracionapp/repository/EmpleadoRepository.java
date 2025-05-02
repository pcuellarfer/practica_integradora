package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
    Optional<Empleado> findByUsuarioId(UUID usuarioId);

    //para buscar por cadenas de caracteres para la busqueda parametrizada por nome
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);
}

