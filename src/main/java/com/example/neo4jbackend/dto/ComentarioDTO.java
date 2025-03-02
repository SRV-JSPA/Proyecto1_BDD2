package com.example.neo4jbackend.dto;

import java.time.LocalDateTime;

public class ComentarioDTO {
    private Long id;
    private String contenido;
    private LocalDateTime fechaComentario;
    private int numLikes;
    private String usernameAutor; 
    private Long idPublicacion;
    

    public ComentarioDTO() {}

    public ComentarioDTO(Long id, String contenido, LocalDateTime fechaComentario, int numLikes, String usernameAutor, Long idPublicacion) {
        this.id = id;
        this.contenido = contenido;
        this.fechaComentario = fechaComentario;
        this.numLikes = numLikes;
        this.usernameAutor = usernameAutor;
        this.idPublicacion = idPublicacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(LocalDateTime fechaComentario) { this.fechaComentario = fechaComentario; }

    public int getNumLikes() { return numLikes; }
    public void setNumLikes(int numLikes) { this.numLikes = numLikes; }

    public String getUsernameAutor() { return usernameAutor; }
    public void setUsernameAutor(String usernameAutor) { this.usernameAutor = usernameAutor; }

    public Long getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(Long idPublicacion) { this.idPublicacion = idPublicacion; }
}
