package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mensaje")
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_mensaje")
    private UUID id;
    private String contenido;
    private LocalDateTime fechaEnvio;

    // Relación N:1 con Colaboracion
    @ManyToOne
    @JoinColumn(name = "colaboracion_id")
    private Colaboracion colaboracion;
}
