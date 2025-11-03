package com.Ecostyle.CrimsonEyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecostyle.CrimsonEyes.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    public boolean existsByEmailAndPassword(String email, String password);

}
