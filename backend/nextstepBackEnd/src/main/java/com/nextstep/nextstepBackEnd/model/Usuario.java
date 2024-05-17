package com.nextstep.nextstepBackEnd.model;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import  javax.validation.constraints.Email;


// Anotación para indicar que es una entidad
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    // Generamos el id automáticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Columna id
    @Column(name = "id")
    private Integer id;
    // Columna nombre
    @Column(name = "nombre")
    private String nombre;
    // Columna correo
    @Column(name = "correo")
    // El email debe ser válido
    @Email(message = "El email debe ser válido")
    private String email;
    // Columna contraseña
    @Column(name = "contraseña")
    private String contraseña;
    // Columna rol
    @Column(name = "rol")
    // El rol es de tipo enumerated
    @Enumerated(EnumType.STRING)
    private Rol rol;

    // Getters y Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}

