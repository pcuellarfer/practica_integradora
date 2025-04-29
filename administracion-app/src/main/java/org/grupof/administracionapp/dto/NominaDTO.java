package org.grupof.administracionapp.dto;

import org.grupof.administracionapp.entity.embeddable.Periodo;

import java.util.UUID;
import java.math.BigDecimal;

public class NominaDTO {
    private UUID id;
    private Periodo periodo;
    private BigDecimal total;
}
