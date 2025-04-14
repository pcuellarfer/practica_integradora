package org.pcuellar.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "colaboracion")
public class Colaboracion {

    @Id
    private UUID id;

    // Empleado 1
    @ManyToOne
    @JoinColumn(name = "empleado_1_id")
    private Empleado empleado1;

    // Empleado 2
    @ManyToOne
    @JoinColumn(name = "empleado_2_id")
    private Empleado empleado2;

    // Relación 1:1 con PeriodoColaboracion
    @OneToOne(mappedBy = "colaboracion", cascade = CascadeType.ALL, optional = false)
    private PeriodoColaboracion periodo;
}