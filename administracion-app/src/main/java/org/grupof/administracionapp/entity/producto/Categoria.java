package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;

    @ManyToMany(mappedBy = "categorias")
    private List<Producto> productos;

    // Constructor para deserializar desde un String
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
