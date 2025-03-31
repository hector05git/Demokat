package com.example.demokat.models;

public class UsuarioModel {
    private int id;
    private String user;
    private String contrasena;

    public UsuarioModel(String user, String contrasena) {
        this.user = user;
        this.contrasena = contrasena;
    }

    public UsuarioModel(int id, String user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioModel(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
