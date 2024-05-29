package com.nextstep.nextstepBackEnd.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"correo"})})

public class Usuario implements UserDetails {

    @Id
    @GeneratedValue
    Integer id;

    String nombre;

    @Basic
    @Column(unique = true, nullable = false)
    String username;

    String password;

    @Enumerated(EnumType.STRING)
    Rol rol;


    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority((rol.name())));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}