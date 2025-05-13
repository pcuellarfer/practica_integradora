package org.grupof.administracionapp.entity.nomina;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.entity.Empleado;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.Periodo;

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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipoVia", column = @Column(name = "empresa_tipo_via")),
            @AttributeOverride(name = "nombreDireccion", column = @Column(name = "empresa_nombre_direccion")),
            @AttributeOverride(name = "numeroDireccion", column = @Column(name = "empresa_numero_direccion")),
            @AttributeOverride(name = "portal", column = @Column(name = "empresa_portal")),
            @AttributeOverride(name = "planta", column = @Column(name = "empresa_planta")),
            @AttributeOverride(name = "puerta", column = @Column(name = "empresa_puerta")),
            @AttributeOverride(name = "localidad", column = @Column(name = "empresa_localidad")),
            @AttributeOverride(name = "region", column = @Column(name = "empresa_region")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "empresa_codigo_postal"))
    })
    private Direccion direccion;

    //nombre + apellido de empleado
    private String nombreEmp;

    private String documentoEmpleado;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "tipoVia", column = @Column(name = "empleado_tipo_via")),
            @AttributeOverride(name = "nombreDireccion", column = @Column(name = "empleado_nombre_direccion")),
            @AttributeOverride(name = "numeroDireccion", column = @Column(name = "empleado_numero_direccion")),
            @AttributeOverride(name = "portal", column = @Column(name = "empleado_portal")),
            @AttributeOverride(name = "planta", column = @Column(name = "empleado_planta")),
            @AttributeOverride(name = "puerta", column = @Column(name = "empleado_puerta")),
            @AttributeOverride(name = "localidad", column = @Column(name = "empleado_localidad")),
            @AttributeOverride(name = "region", column = @Column(name = "empleado_region")),
            @AttributeOverride(name = "codigoPostal", column = @Column(name = "empleado_codigo_postal"))
    })
    private Direccion direccionEmpleado;

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