package com.example.demo.services;

import org.springframework.stereotype.Service;


@Service
public class CalcolatriceService {

    public int somma(int a, int b) {
        return a + b;
    }

    public int differenza(int a, int b) {
        return a - b;
    }

    public int moltiplicazione(int a, int b) {
        return a * b;
    }

    public int divisione(int a, int b) {
        if (b != 0) {
            return a / b;
        } else {
            throw new IllegalArgumentException("Divisione per zero non consentita.");
        }
    }
}

