package org.pcuellar.administracionapp.repository;

import java.util.UUID;
import org.pcuellar.administracionapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {}

