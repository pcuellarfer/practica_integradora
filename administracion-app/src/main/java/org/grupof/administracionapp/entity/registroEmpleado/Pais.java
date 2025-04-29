package org.grupof.administracionapp.entity.registroEmpleado;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nombre;
    private String prefijoTelefonico;

    //metodo para insercion inicial
    public Pais(String nombre, String prefijoTelefonico) {
        this.nombre = nombre;
        this.prefijoTelefonico = prefijoTelefonico;
    }

}
