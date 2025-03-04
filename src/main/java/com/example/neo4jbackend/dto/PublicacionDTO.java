package com.example.neo4jbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class PublicacionDTO {
    private Long id;  
    private String contenido;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") 
    private LocalDateTime fechaPublicacion;
    
    private String imagenUrl;
    private List<String> etiquetas;
    private String usernameAutor;
    private Long autorId;

    public PublicacionDTO() {}

    public PublicacionDTO(Long id, String contenido, LocalDateTime fechaPublicacion, String imagenUrl, 
                          List<String> etiquetas, String usernameAutor, Long autorId) {
        this.id = id;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenUrl = imagenUrl;
        this.etiquetas = etiquetas;
        this.usernameAutor = usernameAutor;
        this.autorId = autorId;
    }

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

    public String getUsernameAutor() { return usernameAutor; }
    public void setUsernameAutor(String usernameAutor) { this.usernameAutor = usernameAutor; }

    public Long getAutorId() { return autorId; } 
    public void setAutorId(Long autorId) { this.autorId = autorId; } 
}
