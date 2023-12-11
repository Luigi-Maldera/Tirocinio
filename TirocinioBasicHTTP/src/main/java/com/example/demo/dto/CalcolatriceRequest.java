package com.example.demo.dto;

public class CalcolatriceRequest {

    private int parametro1;
    private int parametro2;

    // costruttori, getter e setter

    public CalcolatriceRequest() {
    }

    public CalcolatriceRequest(int parametro1, int parametro2) {
        this.parametro1 = parametro1;
        this.parametro2 = parametro2;
    }

    public int getParametro1() {
        return parametro1;
    }

    public void setParametro1(int parametro1) {
        this.parametro1 = parametro1;
    }

    public int getParametro2() {
        return parametro2;
    }

    public void setParametro2(int parametro2) {
        this.parametro2 = parametro2;
    }
}
