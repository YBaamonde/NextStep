package com.nextstep.nextstepBackEnd.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Boolean recurrente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10) // Ahora es nullable
    private Frecuencia frecuencia; // Se asignará solo si recurrente = true

    public enum Frecuencia {
        DIARIA, SEMANAL, MENSUAL, ANUAL
    }

}