package com.Ecostyle.CrimsonEyes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecostyle.CrimsonEyes.model.Persona;
import com.Ecostyle.CrimsonEyes.repository.PersonaRepository;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public void almacenar(Persona persona) {
        personaRepository.save(persona);
    }
}
