package com.Ecostyle.CrimsonEyes.dto;

public class UsuarioResponse {
    private String estado;
    private UsuarioDTO usuarioDTO;
    private String mensaje;

    public UsuarioResponse() {
        estado = "";
        usuarioDTO = new UsuarioDTO();
        mensaje = "";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
}
