package com.example.demo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;
import com.example.demo.services.CalcolatriceService;

@RestController
@RequestMapping("api")
public class CalcolatriceController {
	
	@Autowired
    private CalcolatriceService calcolatriceService;
	
	@Autowired
    private PersonaRepository personaRepository;

	@PostMapping("/somma")
    public ResponseEntity<Integer> somma(@RequestBody Map<String, Integer> requestBody, @RequestHeader("token") String token) {
        if (verificaToken(token)) {
            int risultato = calcolatriceService.somma(requestBody.get("parametro1"), requestBody.get("parametro2"));
            return ResponseEntity.ok(risultato);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/differenza")
    public ResponseEntity<Integer> differenza(@RequestBody Map<String, Integer> requestBody, @RequestHeader("token") String token) {
        if (verificaToken(token)) {
            int risultato = calcolatriceService.differenza(requestBody.get("parametro1"), requestBody.get("parametro2"));
            return ResponseEntity.ok(risultato);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/moltiplicazione")
    public ResponseEntity<Integer> moltiplicazione(@RequestBody Map<String, Integer> requestBody, @RequestHeader("token") String token) {
        if (verificaToken(token)) {
            int risultato = calcolatriceService.moltiplicazione(requestBody.get("parametro1"), requestBody.get("parametro2"));
            return ResponseEntity.ok(risultato);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/divisione")
    public ResponseEntity<Integer> divisione(@RequestBody Map<String, Integer> requestBody, @RequestHeader("token") String token) {
        if (verificaToken(token)) {
            try {
                int risultato = calcolatriceService.divisione(requestBody.get("parametro1"), requestBody.get("parametro2"));
                return ResponseEntity.ok(risultato);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    private boolean verificaToken(String token) {
        Persona persona = personaRepository.findByToken(token);
        return persona != null;
    }
}
