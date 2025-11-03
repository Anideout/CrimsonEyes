package com.Ecostyle.CrimsonEyes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.dto.RecetaResponse;
import com.Ecostyle.CrimsonEyes.model.Receta;
import com.Ecostyle.CrimsonEyes.repository.RecetaRepository;
import com.Ecostyle.CrimsonEyes.util.Util;


@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public ResponseEntity<?> almacenar(Receta receta) {
        Util util = new Util();
        RecetaResponse response = new RecetaResponse();

        Receta validacion = recetaRepository.findByTitle(receta.getTitle());
        if(validacion == null) {

            receta.setId(util.generarID());
            recetaRepository.save(receta);

            response.setEstado("ok");
            response.setMensaje(("Receta: " + receta.getTitle() + " almacenada con exito"));
            response.setReceta(receta);
            return ResponseEntity.ok(response);
        
        } else {
            response.setEstado("Error");
            response.setMensaje("Receta" + receta.getTitle() + " ya existe en la base de datos");
            response.setReceta(new Receta());

            return ResponseEntity.ok(response);
        }
    }
}

