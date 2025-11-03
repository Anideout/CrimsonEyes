package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.dto.LoginDTO;
import com.Ecostyle.CrimsonEyes.dto.UsuarioDTO;
import com.Ecostyle.CrimsonEyes.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

 

    @GetMapping
    public List<UsuarioDTO> usuarioListar() {
        return usuarioService.usuarioListar();
    }

    @PostMapping("/login")
    public UsuarioDTO validarCredenciales(@RequestBody LoginDTO dto) {
        return usuarioService.validarCredenciales(dto);
    }

    @PostMapping("register")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO dto) {
        return usuarioService.almacenar(dto);
    }

}
