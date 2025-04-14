package org.pcuellar.administracionapp.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductoDTO {
    private UUID id;
    private String nombre;
    private BigDecimal precio;
    private String categoria;
    private int cantidad;
}
