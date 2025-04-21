package org.pcuellar.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "empleados")
public class Empleado{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_empleado")
    private UUID id;

    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String genero;

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

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
