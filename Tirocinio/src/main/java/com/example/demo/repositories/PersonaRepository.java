package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.model.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long> {
    Persona findByUsernameAndPassword(String username, String password);
    Persona findByToken(String token);
    Persona findByUsername(String username);
}
