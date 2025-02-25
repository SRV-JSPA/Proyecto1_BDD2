package com.example.neo4jbackend.model;

import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;
import java.util.List;

@Node("Comentario")
public class Comentario {
    @Id @GeneratedValue
    private Long id;
    private String contenido;
    private LocalDateTime fechaComentario;
    private int numLikes;

    @Relationship(type = "COMENTA", direction = Relationship.Direction.INCOMING)
    private Persona autor;

    @Relationship(type = "PERTENECE_A", direction = Relationship.Direction.OUTGOING)
    private Publicacion publicacion;

    @Relationship(type = "DA_LIKE", direction = Relationship.Direction.INCOMING)
    private List<Persona> likes;

    public Comentario() {}

    public Comentario(String contenido, LocalDateTime fechaComentario, int numLikes, Persona autor, Publicacion publicacion) {
        this.contenido = contenido;
        this.fechaComentario = fechaComentario;
        this.numLikes = numLikes;
        this.autor = autor;
        this.publicacion = publicacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(LocalDateTime fechaComentario) { this.fechaComentario = fechaComentario; }

    public int getNumLikes() { return numLikes; }
    public void setNumLikes(int numLikes) { this.numLikes = numLikes; }

    public Persona getAutor() { return autor; }
    public void setAutor(Persona autor) { this.autor = autor; }

    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }

    public List<Persona> getLikes() { return likes; }
    public void setLikes(List<Persona> likes) { this.likes = likes; }
}
