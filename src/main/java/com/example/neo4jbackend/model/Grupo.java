package com.example.neo4jbackend.model;

import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDate;
import java.util.List;

@Node("Grupo")
public class Grupo {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaCreacion;
    private String privacidad; 

    @Relationship(type = "CREADO_POR", direction = Relationship.Direction.OUTGOING)
    private Persona creador;

    @Relationship(type = "MIEMBRO_DE", direction = Relationship.Direction.INCOMING)
    private List<Persona> miembros;

    @Relationship(type = "MODERA", direction = Relationship.Direction.INCOMING)
    private List<Persona> moderadores;

    public Grupo() {}

    public Grupo(String nombre, String descripcion, LocalDate fechaCreacion, String privacidad, Persona creador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.privacidad = privacidad;
        this.creador = creador;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getPrivacidad() { return privacidad; }
    public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

    public Persona getCreador() { return creador; }
    public void setCreador(Persona creador) { this.creador = creador; }

    public List<Persona> getMiembros() { return miembros; }
    public void setMiembros(List<Persona> miembros) { this.miembros = miembros; }

    public List<Persona> getModeradores() { return moderadores; }
    public void setModeradores(List<Persona> moderadores) { this.moderadores = moderadores; }
}
