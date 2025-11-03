package com.Ecostyle.CrimsonEyes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ecostyle.CrimsonEyes.model.Receta;
import com.Ecostyle.CrimsonEyes.service.RecetaService;

@RestController
@RequestMapping("/recetas")
public class RecetaController {


    @Autowired
    private RecetaService recetaService;

    @PostMapping
    public ResponseEntity<?> almacenar(@RequestBody Receta receta) {
        return recetaService.almacenar(receta);
    }
}
