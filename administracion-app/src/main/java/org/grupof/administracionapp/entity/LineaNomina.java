package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "linea_nomina")
public class LineaNomina {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_linea_nomina")
    private UUID id;

    private String concepto;

    //private BigDecimal cantidad;

    private BigDecimal importe;

    @ManyToOne
    private Nomina nomina;
}