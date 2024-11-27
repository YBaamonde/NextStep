package com.nextstep.nextstepBackEnd.model.notif;

import com.nextstep.nextstepBackEnd.model.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "notificacion_config")
@AllArgsConstructor
public class NotificacionConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    // Configuración para notificaciones por Email
    private boolean emailActivas;
    private int emailDiasAntes; // Días antes del evento para enviar notificaciones por email

    // Configuración para notificaciones In-App
    private boolean inAppActivas;
    private int inAppDiasAntes; // Días antes del evento para enviar notificaciones In-App

    // Constructor con valores por defecto
    public NotificacionConfig() {
        this.emailActivas = true;
        this.emailDiasAntes = 1;
        this.inAppActivas = true;
        this.inAppDiasAntes = 1;
    }
}
