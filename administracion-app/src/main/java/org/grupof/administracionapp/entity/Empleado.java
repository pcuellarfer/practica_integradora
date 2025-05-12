package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.CuentaCorriente;
import org.grupof.administracionapp.entity.embeddable.Direccion;
import org.grupof.administracionapp.entity.embeddable.TarjetaCredito;
import org.grupof.administracionapp.entity.registroEmpleado.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "empleado")
@SecondaryTable(name = "datos_economicos", pkJoinColumns = @PrimaryKeyJoinColumn(name = "empleado_id"))
public class Empleado {

    @Id
    //quitado porque ya viene del DTO al hacer el paso de la foto
    //@GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_empleado")
    private UUID id;

    //paso1 datos personales
    private String nombre;
    private String apellido;

    private String fotoUrl; //guarda la ruta

    @ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

    private LocalDate fechaNacimiento;
    private Integer edad;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    private String comentarios;

    //paso2 datos de contacto
    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;
    private String documento;
    private String prefijoTelefono;
    private Integer telefono;
    //opcionalmente otros telefonos de contacto?

    @Embedded
    private Direccion direccion;

    //paso3 datos profesionales
    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToMany
    @JoinTable(
            name = "empleado_especialidad",
            joinColumns = @JoinColumn(name = "empleado_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    private Set<Especialidad> especialidades;

    //paso 4 datos economicos - en secondary tablke
    @AttributeOverride(name = "banco", column = @Column(table = "datos_economicos"))
    @AttributeOverride(name = "numCuenta", column = @Column(table = "datos_economicos"))
    @Embedded
    private CuentaCorriente cuentaCorriente;

    @Column(precision = 10, scale = 2, table = "datos_economicos")  // 10 digitos con 2 decimales, no se si pedira tanto.
    private BigDecimal salario;
    @Column(precision = 10, scale = 2, table = "datos_economicos")
    private BigDecimal comision;

    @Embedded
    @AttributeOverride(name = "tipoTarjeta", column = @Column(table = "datos_economicos"))
    @AttributeOverride(name = "numTarjeta", column = @Column(table = "datos_economicos"))
    @AttributeOverride(name = "mesCaducidad", column = @Column(table = "datos_economicos"))
    @AttributeOverride(name = "anoCaducidad", column = @Column(table = "datos_economicos"))
    @AttributeOverride(name = "cvc", column = @Column(table = "datos_economicos"))
    private TarjetaCredito tarjetaCredito;

    //fin del registro

    //etiquetado
    @ManyToOne
    @JoinColumn(name = "jefe_id")
    private Empleado jefe;

    @OneToMany(mappedBy = "jefe")
    private List<Empleado> subordinados;

    @OneToMany(mappedBy = "jefe")
    private List<Etiqueta> etiquetasDefinidas;

    /////////////

    @Column(name = "fecha_contratacion")
    private LocalDateTime fechaContratacion;

    @Column(name = "fecha_cese")
    private LocalDateTime fechaCese;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
    private List<Nomina> nominas;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
