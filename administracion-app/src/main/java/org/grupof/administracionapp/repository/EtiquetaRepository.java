package org.grupof.administracionapp.repository;


import org.grupof.administracionapp.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, UUID> {
}
