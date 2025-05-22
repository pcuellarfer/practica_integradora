package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mueble extends Producto {
    private String tipoMadera;
    private String estilo;
    private Dimensiones dimensiones;
}
