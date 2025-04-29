package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.registroEmpleado.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EspecialidadRepository extends JpaRepository<Especialidad, UUID> {
}
