package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.registroEmpleado.TipoVia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoViaRepository extends JpaRepository<TipoVia, UUID> {
    boolean existsById(UUID id);
}
