package org.pcuellar.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "empleados")
public class Empleado extends Usuario{
    private String departamento;
    private String puesto;
    private BigDecimal salario;

    @Column(name = "fecha_contratacion")
    private LocalDateTime fechaContratacion;

    @Column(name = "fecha_cese")
    private LocalDateTime fechaCese;

    // Relación 1:1 con Nomina
    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Nomina nomina;
}
