package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) //mete el producto tanto en producto como en la tabla subproducto relacionadas con el id de producto
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String descripcion;

    private double precio;

    private String marca;

    private int unidades;

    private int valoracion = 0;

    @ManyToOne //muchos productos pertenecen a un proveedor.
    private Proveedor proveedor;

    @ManyToMany //un producto puede tener varias categorias, una categoria puede tener muchos productos
    @JoinTable(
            name = "producto_categoria",//nombre de la tabla
            joinColumns = @JoinColumn(name = "producto_id"),//clave foranea qe apunta al id producto
            inverseJoinColumns = @JoinColumn(name = "categoria_id")//clave foranea qe apunta al id de la categoria
    )
    private List<Categoria> categorias;
}
