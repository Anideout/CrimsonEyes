package com.Ecostyle.CrimsonEyes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Ecostyle.CrimsonEyes.model.Receta;

public interface RecetaRepository extends JpaRepository<Receta,String>{
    Receta findByTitle(String title);
}
