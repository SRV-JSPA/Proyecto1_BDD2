package com.example.neo4jbackend.dto;

public class SeguirRequest {
    private String usernameSeguidor;
    private String usernameSeguido;

    public SeguirRequest() {}

    public SeguirRequest(String usernameSeguidor, String usernameSeguido) {
        this.usernameSeguidor = usernameSeguidor;
        this.usernameSeguido = usernameSeguido;
    }

    public String getUsernameSeguidor() { return usernameSeguidor; }
    public void setUsernameSeguidor(String usernameSeguidor) { this.usernameSeguidor = usernameSeguidor; }

    public String getUsernameSeguido() { return usernameSeguido; }
    public void setUsernameSeguido(String usernameSeguido) { this.usernameSeguido = usernameSeguido; }
}
