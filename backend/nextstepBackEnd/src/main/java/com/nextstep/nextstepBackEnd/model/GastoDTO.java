package com.nextstep.nextstepBackEnd.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class GastoDTO {
    private Integer id;
    private String nombre;
    private BigDecimal monto;
    private LocalDate fecha;
    private Integer categoriaId;

    // Constructor, getters y setters
    public GastoDTO(Integer id, String nombre, BigDecimal monto, LocalDate fecha, Integer categoriaId) {
        this.id = id;
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
        this.categoriaId = categoriaId;
    }
}

