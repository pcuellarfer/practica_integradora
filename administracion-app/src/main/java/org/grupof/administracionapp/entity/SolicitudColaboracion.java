package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.auxiliar.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "solicitud_colaboracion")
public class SolicitudColaboracion {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_solicitud_colaboracion")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "empleado_solicitante_id")
    private Empleado solicitante;

    @ManyToOne
    @JoinColumn(name = "empleado_receptor_id")
    private Empleado receptor;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime fechaSolicitud;
}
