# 📋 Instrucciones para Corregir la Pantalla de Perfil

## ❌ Problema Actual
```
Error de conexión: java.lang.IllegalStateException: Expected BEGIN_OBJECT but was BEGIN_ARRAY at line 1 column 2 path $
```

**Causa**: El endpoint `/usuario/listar` devuelve un `List<UsuarioDTO>` (array), pero el cliente espera un único objeto `Usuario`.

---

## ✅ Solución

### 1. **BACKEND - UsuarioController.java**

Agrega este nuevo método GET al `UsuarioController`:

```java
// Método NUEVO para obtener un único usuario por email (devuelve UsuarioDTO, NO una lista)
@GetMapping("/perfil/{email}")
public UsuarioDTO obtenerPerfil(@PathVariable String email) {
    return usuarioService.obtenerPorEmail(email);
}
```

**Ubicación en el archivo**: Agrégalo después del método `@GetMapping("/listar")` pero ANTES del `@PostMapping("/agregar")`.

**Resultado final**:
```java
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    // ... código anterior ...

    @GetMapping("/listar")
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    // ⬇️ AGREGAR ESTE NUEVO MÉTODO ⬇️
    @GetMapping("/perfil/{email}")
    public UsuarioDTO obtenerPerfil(@PathVariable String email) {
        return usuarioService.obtenerPorEmail(email);
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> almacenar(@RequestBody UsuarioDTO dto) {
        return usuarioService.almacenar(dto);
    }
    
    // ... resto del código ...
}
```

---

### 2. **BACKEND - UsuarioService.java**

Verifica que ya exista el método `obtenerPorEmail()`. Según tu código, **YA EXISTE** en tu servicio, así que no necesitas cambios aquí. ✅

El método ya está implementado correctamente:
```java
public UsuarioDTO obtenerPorEmail(String email) {
    UsuarioDTO usuarioDTO = new UsuarioDTO();
    
    if (!usuarioRepository.existsById(email)) {
        return usuarioDTO;
    }

    Usuario usuario = usuarioRepository.findById(email).get();
    
    usuarioDTO.setEmail(usuario.getEmail());
    usuarioDTO.setPassword("*************");
    
    if (usuario.getPersona() != null) {
        usuarioDTO.setRut(usuario.getPersona().getRut());
        usuarioDTO.setNombre(usuario.getPersona().getNombre());
    } else {
        usuarioDTO.setRut("");
        usuarioDTO.setNombre("");
    }
    
    return usuarioDTO;
}
```

---

### 3. **FRONTEND - Ya está actualizado** ✅

El archivo `ApiService.kt` ya fue actualizado automáticamente para usar la nueva ruta:

```kotlin
@GET("usuario/perfil/{email}")
fun obtenerPerfil(@Path("email") email: String): Call<Usuario>

@GET("usuario/perfil")
fun obtenerPerfilByEmail(@Query("email") email: String): Call<Usuario>
```

---

## 🎯 Resumen de Cambios

| Componente | Cambio | Estado |
|-----------|--------|--------|
| Backend Controller | Agregar método `@GetMapping("/perfil/{email}")` | ❌ PENDIENTE |
| Backend Service | Usar método `obtenerPorEmail()` existente | ✅ YA EXISTE |
| Frontend ApiService | Cambiar rutas a `/usuario/perfil` | ✅ HECHO |
| Frontend ProfileViewModel | Usar nuevos endpoints | ✅ HECHO |

---

## 🚀 Pasos para Implementar

1. **Abre** `UsuarioController.java` en tu backend
2. **Copia** el método `@GetMapping("/perfil/{email}")` (ver arriba)
3. **Pégalo** después del método `listar()` 
4. **Guarda** los cambios
5. **Reinicia** el servidor Spring Boot
6. **Prueba** ejecutando la app Android

---

## 📱 Comportamiento Esperado

### Pantalla de Perfil - ANTES (❌ Roto)
- Mostraba solo "Correo"
- Los campos de Nombre y RUT estaban vacíos
- Error: "Expected BEGIN_OBJECT but was BEGIN_ARRAY"

### Pantalla de Perfil - DESPUÉS (✅ Funcional)
- ✅ Muestra el correo del usuario autenticado (NO EDITABLE)
- ✅ Muestra el nombre del usuario (EDITABLE)
- ✅ Muestra el RUT del usuario (NO EDITABLE)
- ✅ Permite cambiar contraseña (EDITABLE con validaciones)
- ✅ Valida que la contraseña actual sea correcta
- ✅ Valida que las nuevas contraseñas coincidan
- ✅ Solo permite editar Nombre y Contraseña

---

## 🔍 Campos Editables vs No Editables

| Campo | Editable | Razón |
|-------|----------|-------|
| Email | ❌ NO | Identificador único del usuario |
| Nombre | ✅ SÍ | El usuario puede actualizar su nombre |
| RUT | ❌ NO | Identificador de identidad, no debería cambiar |
| Contraseña | ✅ SÍ | El usuario puede cambiar su contraseña |

---

## 🛡️ Validaciones Implementadas

### Para Nombre:
- ✅ No puede estar vacío
- ✅ Mínimo 3 caracteres

### Para Contraseña:
- ✅ Si intenta cambiar contraseña, debe ingresar la actual
- ✅ Nueva contraseña mínimo 6 caracteres
- ✅ Las contraseñas nuevas deben coincidir
- ✅ Valida que la contraseña actual sea correcta antes de cambiar

---

## 💾 Formato de Datos Enviados al Servidor

Cuando el usuario hace clic en "Guardar Cambios", se envía:

```json
{
  "email": "usuario@example.com",
  "nombre": "Nuevo Nombre",
  "rut": "12.345.678-9",
  "password": "nuevaPassword123"
}
```

**Nota**: El RUT siempre se mantiene igual (no cambia), incluso si el usuario intenta modificarlo.

---

## 🐛 Si Sigue Sin Funcionar

Verifica:

1. ✅ El endpoint nuevo está en el Controller
2. ✅ El método `obtenerPorEmail()` existe en el Service
3. ✅ La app móvil tiene los endpoints actualizados en `ApiService.kt`
4. ✅ El servidor Spring Boot está reiniciado
5. ✅ La URL base en `RetrofitProvider` es correcta

---

## 📝 Notas Importantes

- El método `obtenerPorEmail()` retorna contraseña como `"*************"` (enmascarada) para seguridad
- La contraseña real se compara en el servidor durante la validación
- El email NO se puede cambiar desde el perfil
- El RUT NO se puede cambiar desde el perfil

