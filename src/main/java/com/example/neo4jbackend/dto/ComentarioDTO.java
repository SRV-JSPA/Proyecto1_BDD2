package com.example.neo4jbackend.dto;

import java.time.LocalDateTime;

public class ComentarioDTO {
    private String contenido;
    private LocalDateTime fechaComentario;
    private String usernameAutor;
    private Long idPublicacion;

    public ComentarioDTO() {}

    public ComentarioDTO(String contenido, LocalDateTime fechaComentario, String usernameAutor, Long idPublicacion) {
        this.contenido = contenido;
        this.fechaComentario = fechaComentario;
        this.usernameAutor = usernameAutor;
        this.idPublicacion = idPublicacion;
    }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(LocalDateTime fechaComentario) { this.fechaComentario = fechaComentario; }

    public String getUsernameAutor() { return usernameAutor; }
    public void setUsernameAutor(String usernameAutor) { this.usernameAutor = usernameAutor; }

    public Long getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(Long idPublicacion) { this.idPublicacion = idPublicacion; }
}
