package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.LineaNomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LineaNominaRepository extends JpaRepository<LineaNomina, UUID> {

    List<LineaNomina> findByNomina_Id(UUID nominaId);

    void deleteByNomina_Id(UUID nominaId);
}