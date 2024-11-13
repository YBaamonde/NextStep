package com.nextstep.nextstepBackEnd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PagoDTO {
    private Integer id;
    private String nombre;
    private BigDecimal monto;
    private LocalDate fecha;
    private Boolean recurrente;
    private Pago.Frecuencia frecuencia;  // nullable

    public PagoDTO(Integer id, String nombre, BigDecimal monto, LocalDate fecha, Boolean recurrente, Pago.Frecuencia frecuencia) {
        this.id = id;
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.recurrente = recurrente;
        this.frecuencia = frecuencia;
    }
}


