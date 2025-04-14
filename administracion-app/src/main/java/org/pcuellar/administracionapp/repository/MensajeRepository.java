package org.pcuellar.administracionapp.repository;

import java.util.UUID;
import org.pcuellar.administracionapp.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, UUID> {}

