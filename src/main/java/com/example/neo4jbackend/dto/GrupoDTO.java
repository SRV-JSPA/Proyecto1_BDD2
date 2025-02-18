package com.example.neo4jbackend.dto;

import java.time.LocalDate;

public class GrupoDTO {
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    private String privacidad;
    private String usernameCreador;

    public GrupoDTO() {}

    public GrupoDTO(String nombre, String descripcion, LocalDate fechaCreacion, String privacidad, String usernameCreador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.privacidad = privacidad;
        this.usernameCreador = usernameCreador;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getPrivacidad() { return privacidad; }
    public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

    public String getUsernameCreador() { return usernameCreador; }
    public void setUsernameCreador(String usernameCreador) { this.usernameCreador = usernameCreador; }
}
