package com.example.neo4jbackend.dto;

public class EtiquetaDTO {
    private String nombre;
    private int popularidad;

    public EtiquetaDTO() {}

    public EtiquetaDTO(String nombre, int popularidad) {
        this.nombre = nombre;
        this.popularidad = popularidad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPopularidad() { return popularidad; }
    public void setPopularidad(int popularidad) { this.popularidad = popularidad; }
}
