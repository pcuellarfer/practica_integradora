package org.grupof.administracionapp.repository;


import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EtiquetaRepository extends JpaRepository<Etiqueta, UUID> {
    boolean existsByJefeAndTexto(Empleado jefe, String texto);
    List<Etiqueta> findByEmpleadosEtiquetados_Id(UUID empleadoId);

}
