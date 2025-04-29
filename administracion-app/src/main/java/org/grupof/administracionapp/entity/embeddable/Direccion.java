package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class Direccion {

    private UUID tipoVia;
    private String nombreDireccion;
    private Integer numeroDireccion;
    private String portal;
    public Integer planta;
    public String puerta;
    private String localidad;
    private String region;
    private Integer codigoPostal;
}
