package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.registroEmpleado.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaisRepository extends JpaRepository<Pais, UUID> {
}