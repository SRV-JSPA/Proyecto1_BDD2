package com.example.neo4jbackend.model;

import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Node("Etiqueta")
public class Etiqueta {
    @Id
    private String nombre;
    private int popularidad;

    @Relationship(type = "TIENE_HASHTAG", direction = Relationship.Direction.INCOMING)
    private List<Publicacion> publicaciones;

    public Etiqueta() {}

    public Etiqueta(String nombre, int popularidad) {
        this.nombre = nombre;
        this.popularidad = popularidad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPopularidad() { return popularidad; }
    public void setPopularidad(int popularidad) { this.popularidad = popularidad; }

    public List<Publicacion> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<Publicacion> publicaciones) { this.publicaciones = publicaciones; }
}
