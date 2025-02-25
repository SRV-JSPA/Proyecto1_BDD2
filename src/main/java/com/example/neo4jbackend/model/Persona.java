package com.example.neo4jbackend.model;

import org.springframework.data.neo4j.core.schema.*;
import java.time.LocalDate;
import java.util.List;

@Node("Persona")
public class Persona {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String username;
    private String email;
    private String password;
    private LocalDate fechaRegistro; 
    private String biografia;
    private List<String> intereses;
    private boolean cuentaVerificada;

    @Relationship(type = "SIGUE_A", direction = Relationship.Direction.OUTGOING)
    private List<Persona> seguidos;

    @Relationship(type = "SIGUE_A", direction = Relationship.Direction.INCOMING)
    private List<Persona> seguidores;

    public Persona() {}

    public Persona(String nombre, String username, String email, String password, LocalDate fechaRegistro, 
                   String biografia, List<String> intereses, boolean cuentaVerificada) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.biografia = biografia;
        this.intereses = intereses;
        this.cuentaVerificada = cuentaVerificada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public List<String> getIntereses() { return intereses; }
    public void setIntereses(List<String> intereses) { this.intereses = intereses; }

    public boolean isCuentaVerificada() { return cuentaVerificada; }
    public void setCuentaVerificada(boolean cuentaVerificada) { this.cuentaVerificada = cuentaVerificada; }

    public List<Persona> getSeguidos() { return seguidos; }
    public void setSeguidos(List<Persona> seguidos) { this.seguidos = seguidos; }

    public List<Persona> getSeguidores() { return seguidores; }
    public void setSeguidores(List<Persona> seguidores) { this.seguidores = seguidores; }
}
