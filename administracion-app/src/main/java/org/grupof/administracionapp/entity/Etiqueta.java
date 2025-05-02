package org.grupof.administracionapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"jefe_id", "texto"}))
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String texto;

    @ManyToOne
    @JoinColumn(name = "jefe_id")
    private Empleado jefe;

    @ManyToMany
    @JoinTable(
            name = "etiqueta_empleado",
            joinColumns = @JoinColumn(name = "etiqueta_id"),
            inverseJoinColumns = @JoinColumn(name = "empleado_id")
    )
    private List<Empleado> empleadosEtiquetados = new ArrayList<>();
}
