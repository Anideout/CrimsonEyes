package com.Ecostyle.CrimsonEyes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "persona")
public class Persona {
    @Id
    public String rut;
    public String nombre;
    public int telefono;

    @OneToOne(mappedBy = "persona")
    public Usuario usuario;

    public Persona() {
        this.rut = "";
        this.nombre = "";
        this.telefono = 0;
        // No inicializar usuario aquí para evitar recursión infinita
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

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

}
