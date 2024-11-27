package com.nextstep.nextstepBackEnd.model.notif;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InAppNotifDTO {
    private Integer id;
    private String titulo;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLeido;
    private Integer pagoId;
}
