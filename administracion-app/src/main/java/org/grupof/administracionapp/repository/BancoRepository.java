package org.grupof.administracionapp.repository;

import org.grupof.administracionapp.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BancoRepository extends JpaRepository<Banco, UUID> {
}
