/*
 * CAMBIOS NECESARIOS EN EL BACKEND
 * ================================
 *
 * Este archivo contiene el código que necesitas agregar/modificar en tu backend.
 */

// ============================================
// 1. ACTUALIZAR: UsuarioController.java
// ============================================

package com.Ecostyle.CrimsonEyes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * GET /usuario/listar
     * Devuelve LISTA de todos los usuarios
     * Este endpoint devuelve un ARRAY
     */
    @GetMapping("/listar")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    /**
     * ⭐ NUEVO ENDPOINT ⭐
     * GET /usuario/perfil/{email}
     * Devuelve UN ÚNICO usuario por email
     * Este endpoint devuelve un OBJETO (no array)
     *
     * Este es el que el app móvil usa para cargar el perfil del usuario
     */
    @GetMapping("/perfil/{email}")
    public UsuarioDTO obtenerPerfil(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email);
    }

    /**
     * POST /usuario/agregar
     * Crea un nuevo usuario
     */
    @PostMapping("/agregar")
    public ResponseEntity<?> almacenar(@RequestBody UsuarioDTO dto) {
        return usuarioService.almacenar(dto);
    }

    /**
     * POST /usuario/login
     * Valida las credenciales del usuario
     */
    @PostMapping("/login")
    public UsuarioDTO validarCredenciales(@RequestBody LoginDTO dto) {
        return usuarioService.validarCredenciales(dto);
    }

    /**
     * POST /usuario/register
     * Registra un nuevo usuario (alias de /agregar)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO dto) {
        return usuarioService.almacenar(dto);
    }

    /**
     * PUT /usuario/editar/{email}
     * Actualiza el perfil del usuario
     *
     * IMPORTANTE: El email original se pasa en la ruta (@PathVariable)
     * El nuevo email (si cambió) va en el body (@RequestBody)
     */
    @PutMapping("/editar/{email}")
    public ResponseEntity<?> editarPerfil(@PathVariable String email, @RequestBody UsuarioDTO dto) {
        return usuarioService.editarPerfil(email, dto);
    }
}


// ============================================
// 2. VERIFICAR: UsuarioService.java
// ============================================

/*
 * El método obtenerPorEmail() YA EXISTE en tu servicio
 * y está correctamente implementado.
 *
 * No necesitas hacer cambios aquí, solo verificar que exista.
 *
 * El método debe verse así:
 */

public UsuarioDTO obtenerPorEmail(String email) {
    UsuarioDTO usuarioDTO = new UsuarioDTO();

    if (!usuarioRepository.existsById(email)) {
        return usuarioDTO; // Retorna vacío si no existe
    }

    Usuario usuario = usuarioRepository.findById(email).get();

    usuarioDTO.setEmail(usuario.getEmail());
    usuarioDTO.setPassword("*************"); // Contraseña enmascarada por seguridad

    if (usuario.getPersona() != null) {
        usuarioDTO.setRut(usuario.getPersona().getRut());
        usuarioDTO.setNombre(usuario.getPersona().getNombre());
    } else {
        usuarioDTO.setRut("");
        usuarioDTO.setNombre("");
    }

    return usuarioDTO;
}


// ============================================
// 3. MAPEO DE ENDPOINTS
// ============================================

/*
 * Después de estos cambios, tendrás estos endpoints disponibles:
 *
 * GET    /usuario/listar                 → Retorna List<UsuarioDTO> (ARRAY)
 * GET    /usuario/perfil/{email}         → Retorna UsuarioDTO (OBJETO) ⭐ NUEVO
 * POST   /usuario/agregar                → Crea usuario
 * POST   /usuario/login                  → Valida credenciales
 * POST   /usuario/register               → Registra usuario (alias)
 * PUT    /usuario/editar/{email}         → Actualiza perfil
 */


// ============================================
// 4. DIFERENCIA CLAVE
// ============================================

/*
 * ANTES (❌ ROMPE LA APP):
 * GET /usuario/listar/{email}  → Retorna List<UsuarioDTO> (ARRAY)
 *
 * DESPUÉS (✅ FUNCIONA):
 * GET /usuario/perfil/{email}  → Retorna UsuarioDTO (OBJETO ÚNICO)
 *
 * El app móvil ESPERA un OBJETO, no un ARRAY
 * Por eso daba el error:
 * "Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $"
 */


// ============================================
// 5. RESUMEN DE CAMBIOS EN BACKEND
// ============================================

/*
 * 1. Abre: UsuarioController.java
 * 2. Copia el nuevo método @GetMapping("/perfil/{email}")
 * 3. Pégalo después del @GetMapping("/listar")
 * 4. Guarda el archivo
 * 5. Reinicia el servidor Spring Boot
 * 6. Prueba desde la app móvil
 *
 * El rest del código ya está correcto.
 */

