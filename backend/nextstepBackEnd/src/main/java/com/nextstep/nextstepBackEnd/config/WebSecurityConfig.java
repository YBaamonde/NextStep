package com.nextstep.nextstepBackEnd.config;

import com.nextstep.nextstepBackEnd.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Deshabilitar CSRF (para sistemas sin estado)
        http.csrf(AbstractHttpConfigurer::disable);

        // Configurar las políticas de autorización
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Permitir libre acceso a las rutas de autenticación
                .requestMatchers("/admin/**").hasAnyAuthority("admin")  // Solo los administradores pueden acceder
                .requestMatchers("/api/protegido/**").authenticated() // Proteger las rutas de la API (pruebas)
                .requestMatchers("/categorias/**").authenticated() // Requiere autenticación para las rutas de categorías
                .requestMatchers("/debug/**").permitAll() // Permitir acceso a rutas de depuración
                .requestMatchers("/perfil/**").authenticated() // Requiere autenticación para las rutas de perfil
                .requestMatchers("/simulacion/**").authenticated() // Requiere autenticación para las rutas de simulación
                .requestMatchers("/gastos/**").authenticated()
                .requestMatchers("/pagos/**").authenticated()
                .requestMatchers("/notificaciones/**").authenticated()
                .requestMatchers("/").authenticated() // Bloquear la ruta raíz si no está autenticado
                .anyRequest().authenticated() // Proteger cualquier otra ruta
        );

        // Establecer la política de sesiones como STATELESS (sin estado) para usar JWT
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        /*
        PARA DEPURAR

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Temporalmente para depuración
        );
         */

        // Configurar el proveedor de autenticación y añadir el filtro JWT
        http.authenticationProvider(authProvider);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración de CORS para permitir solicitudes desde el frontend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080") // Permitir solicitudes desde el frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
