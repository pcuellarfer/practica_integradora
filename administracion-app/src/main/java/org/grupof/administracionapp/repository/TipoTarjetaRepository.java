package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.TipoTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TipoTarjetaRepository extends JpaRepository<TipoTarjeta, UUID> {
}
