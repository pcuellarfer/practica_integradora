package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.registroEmpleado.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartamentoRepository extends JpaRepository<Departamento, UUID> {
}
