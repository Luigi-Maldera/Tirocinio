package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Persona;
import com.example.demo.services.PersonaService;

@RestController
@RequestMapping("/persona")
public class PersonaController {

    private final PersonaService personaService;

    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping("/create")
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        Persona createdPersona = personaService.createPersona(persona);
        return new ResponseEntity<>(createdPersona, HttpStatus.CREATED);
    }
}
