package com.example.antonio.gesapp.Pojos;

import android.content.ContentValues;

public class Conductor {


    private String placa;
    private String clave;
    public Conductor(){
        
    }

    public Conductor(String placa,String clave) {
        this.placa = placa;
        this.clave = clave;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }







}



