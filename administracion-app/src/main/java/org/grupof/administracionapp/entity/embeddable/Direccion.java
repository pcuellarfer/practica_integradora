package org.grupof.administracionapp.entity.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data

@Embeddable
public class Direccion {

    //tipo via
    private String nombreDireccion;
    private Integer numeroDireccion;
    private String portal;
    public Integer planta;
    public String puerta;
    private String localidad;
    private String region;
    private Integer codigoPostal;
}
