package org.grupof.administracionapp.dto.Empleado;

import lombok.Getter;
import lombok.Setter;
import org.grupof.administracionapp.entity.embeddable.Direccion;

import java.util.UUID;

@Getter
@Setter
public class Paso2ContactoDTO {

    private UUID tipoDocumento;
    private String documento;
    private String prefijoTelefono;
    private Integer telefono;
    //telefono opcional?
    private Direccion direccion;
}
