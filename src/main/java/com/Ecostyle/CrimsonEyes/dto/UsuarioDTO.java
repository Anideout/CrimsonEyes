package com.Ecostyle.CrimsonEyes.dto;

public class UsuarioDTO {
    public String email;
    public String password;
    public String rut;
    public String nombre;

    public UsuarioDTO() {
        this.email = "";
        this.password = "";
        this.rut = "";
        this.nombre = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
