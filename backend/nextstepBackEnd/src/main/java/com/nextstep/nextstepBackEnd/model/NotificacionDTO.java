package com.nextstep.nextstepBackEnd.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionDTO {
    private Integer id;
    private String titulo;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLeido;
    private Integer pagoId;
}
