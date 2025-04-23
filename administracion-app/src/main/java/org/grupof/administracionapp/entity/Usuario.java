package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.auxiliar.TipoUsuario;

import java.io.File;
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

    @Email(message = "El email no es válido.")
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    private boolean estadoBloqueado = false;
    private LocalDateTime bloqueoFechaHora;
    private String motivoBloqueo;

    //no lo pide José Ramon pero se podria usar  "cascade = CascadeType.ALL"
    //para que cuando borre un usuario se borre su empleado
    @OneToOne(mappedBy = "usuario")
    private Empleado empleado;

    private byte[] archivo;
}
