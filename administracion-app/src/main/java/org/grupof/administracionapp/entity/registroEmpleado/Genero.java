package org.grupof.administracionapp.entity.registroEmpleado;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grupof.administracionapp.repository.GeneroRepository;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //femenino, masculino, no binaro, etc
    private String identidad;

    //metodo para insercion inicial
    public Genero(String identidad) {
        this.identidad = identidad;
    }

}
