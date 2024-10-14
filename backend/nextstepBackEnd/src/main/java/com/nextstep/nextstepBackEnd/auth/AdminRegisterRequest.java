package com.nextstep.nextstepBackEnd.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotBlank(message = "El correo no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "El rol no puede estar vacío")
    private String rol;

    /*

    // Metodo simple de validación
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
                email != null && email.length() >= 4 &&
                password != null && password.length() >= 8; // &&
                // rol != null && !rol.trim().isEmpty();
    }

     */
}
