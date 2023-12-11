package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Persona;


@Repository
public interface PersonaRepository extends CrudRepository<Persona, Long> {
    Persona findByUsernameAndPassword(String username, String password);
    Persona findByToken(String token);
    Persona findByUsername(String username);
}
