package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.registroEmpleado.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GeneroRepository extends JpaRepository<Genero, UUID> {
}
