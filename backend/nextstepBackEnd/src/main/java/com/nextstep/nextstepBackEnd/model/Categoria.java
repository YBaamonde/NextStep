package com.nextstep.nextstepBackEnd.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "categoria")
public class Categoria {

    // Getters y Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}


