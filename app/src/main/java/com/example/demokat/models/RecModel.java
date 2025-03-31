package com.example.demokat.models;

import java.time.LocalDate;

public class RecModel {
    private String rec;
    private String titulo;
    private String instrumento;
    private LocalDate fecha;
    private int user_id;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public RecModel(String rec, String titulo, String instrumento,  LocalDate fecha, int user_id) {
        this.rec = rec;
        this.titulo = titulo;
        this.instrumento = instrumento;
        this.user_id = user_id;
        this.fecha = fecha;
    }
}
