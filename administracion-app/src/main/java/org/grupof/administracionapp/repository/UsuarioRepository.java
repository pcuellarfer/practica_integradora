package org.grupof.administracionapp.repository;

import java.util.Optional;
import java.util.UUID;
import org.grupof.administracionapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario>findByNombre(String nombre);
    Optional<Usuario>findByEmail(String email);
}
