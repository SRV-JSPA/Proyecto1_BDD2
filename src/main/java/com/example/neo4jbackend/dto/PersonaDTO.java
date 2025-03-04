package com.example.neo4jbackend.dto;

import java.time.LocalDate;
import java.util.List;

public class PersonaDTO {
    private String nombre;
    private String username;
    private String email;
    private String password;
    private LocalDate fechaRegistro;
    private String biografia;
    private List<String> intereses;
    private boolean cuentaVerificada;

    private boolean esCreador;
    private boolean esEmpresa;
    private boolean esPaginaDeFans;
    private boolean esCantante;

    public PersonaDTO() {}

    public PersonaDTO(String nombre, String username, String email, String password, LocalDate fechaRegistro,
                      String biografia, List<String> intereses, boolean cuentaVerificada,
                      boolean esCreador, boolean esEmpresa, boolean esPaginaDeFans, boolean esCantante) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fechaRegistro = fechaRegistro;
        this.biografia = biografia;
        this.intereses = intereses;
        this.cuentaVerificada = cuentaVerificada;
        this.esCreador = esCreador;
        this.esEmpresa = esEmpresa;
        this.esPaginaDeFans = esPaginaDeFans;
        this.esCantante = esCantante;
    }

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

    public boolean isEsCreador() { return esCreador; }
    public void setEsCreador(boolean esCreador) { this.esCreador = esCreador; }

    public boolean isEsEmpresa() { return esEmpresa; }
    public void setEsEmpresa(boolean esEmpresa) { this.esEmpresa = esEmpresa; }

    public boolean isEsPaginaDeFans() { return esPaginaDeFans; }
    public void setEsPaginaDeFans(boolean esPaginaDeFans) { this.esPaginaDeFans = esPaginaDeFans; }

    public boolean isEsCantante() { return esCantante; }
    public void setEsCantante(boolean esCantante) { this.esCantante = esCantante; }
}
