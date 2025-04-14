package org.pcuellar.administracionapp.repository;

import java.util.UUID;
import org.pcuellar.administracionapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {}
