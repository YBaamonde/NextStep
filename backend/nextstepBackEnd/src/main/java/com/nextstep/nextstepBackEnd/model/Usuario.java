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
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})  // Email debe ser único también
})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(unique = true, nullable = false)
    String username;  // Nombre de usuario

    @Basic
    @Column(unique = true, nullable = false)
    String email;  // Correo electrónico

    @Column(nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    Rol rol;


    // Relación bidireccional con Gasto
    /*
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference // Para que Spring serialice el objeto como un JSON
    private List<Gasto> gastos;
     */

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name())); // Retorna el rol como GrantedAuthority
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
