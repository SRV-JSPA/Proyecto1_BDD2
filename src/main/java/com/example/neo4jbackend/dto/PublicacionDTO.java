package com.example.neo4jbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PublicacionDTO {
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private String imagenUrl;
    private List<String> etiquetas;
    private String usernameAutor;

    public PublicacionDTO() {}

    public PublicacionDTO(String contenido, LocalDateTime fechaPublicacion, String imagenUrl, 
                          List<String> etiquetas, String usernameAutor) {
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenUrl = imagenUrl;
        this.etiquetas = etiquetas;
        this.usernameAutor = usernameAutor;
    }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public List<String> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }

    public String getUsernameAutor() { return usernameAutor; }
    public void setUsernameAutor(String usernameAutor) { this.usernameAutor = usernameAutor; }
}
