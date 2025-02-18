package com.example.neo4jbackend.model;

import org.springframework.data.neo4j.core.schema.*;
import java.time.LocalDateTime;
import java.util.List;

@Node("Publicacion")
public class Publicacion {
    @Id @GeneratedValue
    private Long id;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private String imagenUrl;
    private List<String> etiquetas;
    private int numLikes;
    private int numComentarios;

    @Relationship(type = "PUBLICA", direction = Relationship.Direction.INCOMING)
    private Persona autor;

    @Relationship(type = "DA_LIKE", direction = Relationship.Direction.INCOMING)
    private List<Persona> likes;

    @Relationship(type = "TIENE_HASHTAG", direction = Relationship.Direction.OUTGOING)
    private List<Etiqueta> hashtags;

    public Publicacion() {}

    public Publicacion(String contenido, LocalDateTime fechaPublicacion, String imagenUrl, 
                       List<String> etiquetas, int numLikes, int numComentarios, Persona autor) {
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenUrl = imagenUrl;
        this.etiquetas = etiquetas;
        this.numLikes = numLikes;
        this.numComentarios = numComentarios;
        this.autor = autor;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public List<String> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }

    public int getNumLikes() { return numLikes; }
    public void setNumLikes(int numLikes) { this.numLikes = numLikes; }

    public int getNumComentarios() { return numComentarios; }
    public void setNumComentarios(int numComentarios) { this.numComentarios = numComentarios; }

    public Persona getAutor() { return autor; }
    public void setAutor(Persona autor) { this.autor = autor; }

    public List<Persona> getLikes() { return likes; }
    public void setLikes(List<Persona> likes) { this.likes = likes; }

    public List<Etiqueta> getHashtags() { return hashtags; }
    public void setHashtags(List<Etiqueta> hashtags) { this.hashtags = hashtags; }
}
