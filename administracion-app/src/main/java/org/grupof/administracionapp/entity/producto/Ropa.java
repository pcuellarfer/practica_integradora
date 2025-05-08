package org.grupof.administracionapp.entity.producto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Ropa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String talla;


}
