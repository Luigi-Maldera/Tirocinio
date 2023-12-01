package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;

@RestController
public class HelloController {
	
	@Autowired
    private PersonaRepository personaRepository;

	@GetMapping("/ciao/{id}")
    public String saluta(@PathVariable Long id) {
        // Cerca la persona per ID nel database
        Persona persona = personaRepository.findById(id).orElse(null);

        if (persona != null) {
            return "Ciao " + persona.getNome();
        } else {
            return "Persona non trovata per l'ID: " + id;
        }
    }
	
}
