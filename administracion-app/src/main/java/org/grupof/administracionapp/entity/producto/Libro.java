package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Libro { //extends producto

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String titulo;

    private String autor;

    private String editorial;

    private String Tapa; //entidad tapa?

    private int numPaginas;

    private Boolean segundaMano;
}
