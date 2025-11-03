package com.Ecostyle.CrimsonEyes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.dto.LoginDTO;
import com.Ecostyle.CrimsonEyes.dto.UsuarioDTO;
import com.Ecostyle.CrimsonEyes.dto.UsuarioResponse;
import com.Ecostyle.CrimsonEyes.model.Persona;
import com.Ecostyle.CrimsonEyes.model.Usuario;
import com.Ecostyle.CrimsonEyes.repository.PersonaRepository;
import com.Ecostyle.CrimsonEyes.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    public void almacenarusuario(UsuarioDTO dto) {

    }

    public ResponseEntity<?> almacenar(UsuarioDTO dto) {
        UsuarioResponse response = new UsuarioResponse();

    
        if (usuarioRepository.existsById(dto.getEmail())) {
            response.setEstado("Error");
            response.setMensaje("El usuario con email: " + dto.getEmail() + " ya existe");
            return ResponseEntity.ok(response);
        }
        if (dto.getRut() == null || dto.getRut().trim().isEmpty()) {
                Persona persona = new Persona();
                persona.setNombre(dto.getNombre());
                persona.setRut("TEMP-" + dto.getEmail().replaceAll("[@.]", "-"));
                personaRepository.save(persona);

                Usuario usuario = new Usuario();
                usuario.setEmail(dto.getEmail());
                usuario.setPassword(dto.getPassword());
                usuario.setPersona(persona);

            usuarioRepository.save(usuario);

            // Obtener usuario guardado
            Usuario usuarioGuardado = usuarioRepository.findById(dto.getEmail()).orElse(null);
            UsuarioDTO dtoGuardado = new UsuarioDTO();
            if (usuarioGuardado != null) {
                dtoGuardado.setEmail(usuarioGuardado.getEmail());
                dtoGuardado.setPassword("*************");
                    if (usuarioGuardado.getPersona() != null) {
                        dtoGuardado.setRut(usuarioGuardado.getPersona().getRut());
                        dtoGuardado.setNombre(usuarioGuardado.getPersona().getNombre());
                    }
            }

            System.out.println("[UsuarioService] usuarioGuardado (simple) -> "
                    + (usuarioGuardado != null ? usuarioGuardado.getEmail() : "null"));

            response.setEstado("OK");
            response.setMensaje("Usuario creado correctamente (registro simple)!");
            response.setUsuarioDTO(dtoGuardado);
            return ResponseEntity.ok(response);
        }

        if (personaRepository.existsById(dto.getRut())) {
            response.setEstado("Error");
            response.setMensaje("Rut: " + dto.getRut() + " ya asociado a otro usuario");
            return ResponseEntity.ok(response);
        }
        Persona persona = new Persona();
        Usuario usuario = new Usuario();

        persona.setRut(dto.getRut());
        persona.setNombre(dto.getNombre());

        personaRepository.save(persona);

        usuario.setEmail(dto.getEmail());
        usuario.setPassword((dto.getPassword()));
        usuario.setPersona(persona);

        usuarioRepository.save(usuario);

        // Obtener usuario guardado
        Usuario usuarioGuardado = usuarioRepository.findById(dto.getEmail()).orElse(null);
        UsuarioDTO dtoGuardado = new UsuarioDTO();
        if (usuarioGuardado != null) {
            dtoGuardado.setEmail(usuarioGuardado.getEmail());
            dtoGuardado.setPassword("*************");
            if (usuarioGuardado.getPersona() != null) {
                dtoGuardado.setRut(usuarioGuardado.getPersona().getRut());
                dtoGuardado.setNombre(usuarioGuardado.getPersona().getNombre());
            } else {
                dtoGuardado.setRut("");
                dtoGuardado.setNombre("");
            }
        }

        System.out.println("[UsuarioService] usuarioGuardado -> "
                + (usuarioGuardado != null ? usuarioGuardado.getEmail() : "null") + ", persona -> "
                + (usuarioGuardado != null && usuarioGuardado.getPersona() != null
                        ? usuarioGuardado.getPersona().getRut()
                        : "null"));

        response.setEstado("OK");
        response.setMensaje("Usuario creado correctamente!");
        response.setUsuarioDTO(dtoGuardado);
        return ResponseEntity.ok(response);
    }

    public List<UsuarioDTO> usuarioListar() {
        List<UsuarioDTO> listaFinal = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findAll();

        for (Usuario u : usuarios) {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setPassword("*************");
            dto.setEmail(u.getEmail());

            Persona persona = u.getPersona();
            if (persona != null) {
                dto.setRut(persona.getRut());
                dto.setNombre(persona.getNombre());
            } else {
                dto.setRut("");
                dto.setNombre("");
            }

            listaFinal.add(dto);
        }
        return listaFinal;
    }

    public UsuarioDTO validarCredenciales(LoginDTO dto) {
        boolean validador = usuarioRepository.existsByEmailAndPassword(dto.getEmail(), dto.getPassword());
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        if (validador) {
            Usuario usuario = this.usuarioRepository.findById(dto.getEmail()).get();

            usuarioDTO.setEmail(dto.getEmail());
            usuarioDTO.setPassword("*************");
            usuarioDTO.setRut(usuario.getPersona().getRut());
            usuarioDTO.setNombre(usuario.getPersona().getNombre());

            return usuarioDTO;
        } else {
            return new UsuarioDTO();
        }
    }

}
