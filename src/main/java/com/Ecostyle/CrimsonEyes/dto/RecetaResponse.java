package com.Ecostyle.CrimsonEyes.dto;

import com.Ecostyle.CrimsonEyes.model.Receta;

public class RecetaResponse {
    private String estado;
    private Receta receta;
    private String mensaje;

    public RecetaResponse() {
        this.estado = "";
        this.receta = new Receta();
        this.mensaje = "";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    

}
