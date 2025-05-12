package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.Periodo;
import org.grupof.administracionapp.entity.registroEmpleado.Departamento;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private String nombreEmpresa = "EfeCorp";

    private String CIF = "A12345678";

    //opcional
    private String numSeguridadSocial;

    private LocalDate fechaAltaEmp;

    @Embedded //optional direccion de empresa
    private Direccion direccion;

    //nombre + apellido de empleado
    private String nombreEmp;

    private String documentoEmpleado;

    @Embedded //optional direccion de empresa
    private Direccion direccionEmp;

    //puesto del empleado
    private String perfilProfesional;

    //departamento del empleado
    private String departamento;

    //opcional
    //Si es la nómina de marzo, la cantidad bruta acumulada es la suma de las
    //cantidades brutas cobradas en enero, febrero y marzo.
    private BigDecimal brutoAcumulado;

    //opcional
    private BigDecimal retenciones;

    //opcional
    private BigDecimal cantidadPercibida;

    //información específica a cada nómina

    @Embedded //obligatorio
    private Periodo periodo;

    @OneToMany(mappedBy = "nomina", //lineaNomina es propietaria
            cascade = CascadeType.ALL, //por si se borra una nomina tambien sus lineas
            orphanRemoval = true) //si se borra una linea de la vista tambien de la bbdd
    private List<LineaNomina> lineasNomina;

    //ingresos
    private BigDecimal devengos;

    //retenciones
    private BigDecimal deducciones;

    //siempre positivo
    private BigDecimal salarioNeto;

    @ManyToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;
}