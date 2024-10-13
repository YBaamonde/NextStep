package com.nextstep.nextstepBackEnd.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    // Metodo simple de validación
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty();
    }
}
