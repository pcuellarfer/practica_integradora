package org.pcuellar.administracionapp.dto;

import org.pcuellar.administracionapp.auxiliar.Periodo;

import java.util.UUID;
import java.math.BigDecimal;

public class NominaDTO {
    private UUID id;
    private Periodo periodo;
    private BigDecimal total;
}
