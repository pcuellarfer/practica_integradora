package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String descripcion;

    private double precio;

    private String marca;

    private int unidad;

    private LocalDate fechaFabricacion;

    private Dimensiones dimensiones;
}
