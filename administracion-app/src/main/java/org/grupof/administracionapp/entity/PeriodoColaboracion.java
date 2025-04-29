package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.Periodo;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "periodo_colaboracion")
public class PeriodoColaboracion {

    @Id
    private UUID id;

    private Periodo periodo;

    // Relación 1:1 con Colaboracion
    @OneToOne
    @JoinColumn(name = "colaboracion_id")
    private Colaboracion colaboracion;
}