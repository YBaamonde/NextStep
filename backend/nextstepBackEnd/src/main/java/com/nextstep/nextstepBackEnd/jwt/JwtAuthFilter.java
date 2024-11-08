package com.nextstep.nextstepBackEnd.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Obtener el token desde el encabezado de autorización
            final String token = getTokenFromReq(request);

            // Continuar con el filtro si no hay token en la solicitud
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extraer el nombre de usuario desde el token
            String username = jwtService.getUsernameFromToken(token);

            // Verificar que el usuario está en el token y que aún no está autenticado en el contexto de seguridad
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validar el token
                if (jwtService.isTokenValid(token, userDetails)) {
                    // Extraer roles del token y convertirlos en autoridades
                    List<String> roles = jwtService.getRolesFromToken(token);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // Crear el token de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer el token de autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // Token no válido, continuar con el filtro sin autenticar
                    filterChain.doFilter(request, response);
                    return;
                }
            }

        } catch (Exception ex) {
            // Log para depuración y continuar con el filtro sin autenticar
            System.err.println("Error en el filtro de autenticación JWT: " + ex.getMessage());
        }

        // Continuar con la cadena de filtros en cualquier caso
        filterChain.doFilter(request, response);
    }


    String getTokenFromReq(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7); // Eliminar el prefijo "Bearer " y devolver el token
        }
        return null;
    }
}

