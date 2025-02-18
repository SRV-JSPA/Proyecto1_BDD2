package com.example.neo4jbackend.dto;

public class LikeRequest {
    private String username;
    private Long idPublicacion;
    private Long idComentario; // Agregamos este atributo

    public LikeRequest() {}

    public LikeRequest(String username, Long idPublicacion, Long idComentario) {
        this.username = username;
        this.idPublicacion = idPublicacion;
        this.idComentario = idComentario;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getIdPublicacion() { return idPublicacion; }
    public void setIdPublicacion(Long idPublicacion) { this.idPublicacion = idPublicacion; }

    public Long getIdComentario() { return idComentario; } // Agregamos el getter
    public void setIdComentario(Long idComentario) { this.idComentario = idComentario; } // Agregamos el setter
}
