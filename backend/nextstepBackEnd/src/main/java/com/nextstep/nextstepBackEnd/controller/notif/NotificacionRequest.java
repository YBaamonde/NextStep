package com.nextstep.nextstepBackEnd.controller.notif;

/* ESTA ES UNA CLASE AUXILIAR PARA MANEJAR EL CONTROLADOR DE MANERA SENCILLA */

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class NotificacionRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}

