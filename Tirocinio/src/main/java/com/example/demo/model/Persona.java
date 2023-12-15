package com.example.demo.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Persona implements UserDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String cognome;
    private String indirizzo;
    private String username;
    private String password;
    private String token;

 // costruttori, getter e setter

 public Persona() {
 }

 public Persona(Long id, String nome) {
     this.id = id;
     this.nome = nome;
 }

 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public String getNome() {
     return nome;
 }

 public void setNome(String nome) {
     this.nome = nome;
 }

public String getCognome() {
	return cognome;
}


public void setCognome(String cognome) {
	this.cognome = cognome;
}


public String getIndirizzo() {
	return indirizzo;
}


public void setIndirizzo(String indirizzo) {
	this.indirizzo = indirizzo;
}


public String getUsername() {
	return username;
}


public void setUsername(String username) {
	this.username = username;
}


public String getPassword() {
	return password;
}


public void setPassword(String password) {
	this.password = password;
}


public String getToken() {
	return token;
}


public void setToken(String token) {
	this.token = token;
}

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    // Definire qui eventuali ruoli dell'utente
    return null;
}


@Override
public boolean isAccountNonExpired() {
    return true;
}

@Override
public boolean isAccountNonLocked() {
    return true;
}

@Override
public boolean isCredentialsNonExpired() {
    return true;
}

@Override
public boolean isEnabled() {
    return true;
}
}
