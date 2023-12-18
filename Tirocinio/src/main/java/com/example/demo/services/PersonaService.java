package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Persona createPersona(Persona persona) {
        // Esegui eventuali controlli o elaborazioni prima di salvare la persona
        return personaRepository.save(persona);
    }
}
