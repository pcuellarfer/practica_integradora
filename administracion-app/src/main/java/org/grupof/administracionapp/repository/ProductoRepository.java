package org.grupof.administracionapp.repository;

import java.util.List;
import java.util.UUID;

import org.grupof.administracionapp.entity.producto.Producto;
import org.grupof.administracionapp.entity.producto.enums.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {}

