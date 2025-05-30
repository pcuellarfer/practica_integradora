package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.registroEmpleado.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, UUID> {
    Optional<TipoDocumento> findByTipoIgnoreCase(String tipo);
}
