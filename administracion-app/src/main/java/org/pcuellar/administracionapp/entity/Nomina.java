package org.pcuellar.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pcuellar.administracionapp.auxiliar.Periodo;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "nomina")
public class Nomina {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_nomina")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Periodo periodo;

    private BigDecimal total;

    @OneToOne
    @JoinColumn(name = "FK_empleado", nullable = false)
    private Empleado empleado;

    @OneToMany(mappedBy = "nomina", cascade = CascadeType.ALL)
    private List<LineaNomina> lineas;
}