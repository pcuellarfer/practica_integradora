package org.pcuellar.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pcuellar.administracionapp.auxiliar.TipoUsuario;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_usuario")
    private UUID id;

    private String nombre;
    private String contrasena;
    private String email;

    @Enumerated(EnumType.ORDINAL)
    private TipoUsuario tipoUsuario;

    private boolean estadoBloqueado = false;
    private int intentosFallidos = 0;
    private LocalDateTime bloqueoFechaHora;
    private String motivoBloqueo;
}
