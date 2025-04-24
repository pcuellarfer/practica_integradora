package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PK_empleado")
    private UUID id;

    //paso1 datos personales
    private String nombre;
    private String apellido;
    private String foto;

    @ManyToOne
    @JoinColumn(name = "genero_id")
    private Genero genero;

    private LocalDate fechaNacimiento;
    private int edad;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    private String comentarios;

    //paso2 datos de contacto
    private String tipoDocumento;
    private String documento;
    private String prefijoTelefono;
    private String telefono;
    //opcionalmente otros telefonos de contacto?
    private String direccion;
    private String tipovia;
    private String nombreVia;
    private String numVia;
    private String portal;
    private String planta;
    private String puerta;
    private String localidad;
    private String region;
    private String codigoPostal;

    //paso3 datos profesionales
    private String departamento;
    private String especialidades;

    //paso 4 datos economicos
    private String cuenta;
    private String banco;
    private String numCuenta;
    private String salario;
    private String comision;
    private String tarjetaCredito;
    private String tipoTarjeta;
    private String numTarjeta;
    private String caducidad;
    private String mes;
    private String ano;
    private String cvc;

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
