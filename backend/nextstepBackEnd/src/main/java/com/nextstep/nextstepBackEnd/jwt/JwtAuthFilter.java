package com.nextstep.nextstepBackEnd.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtenemos el token del header
        final String token = request.getHeader("Authorization");

        // Si el token es nulo:
        if (token == null){
        filterChain.doFilter(request, response);
        return;
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        final String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
