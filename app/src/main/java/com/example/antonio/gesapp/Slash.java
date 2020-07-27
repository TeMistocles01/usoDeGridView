package com.example.antonio.gesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Slash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);

//TimerTask este metodo es una tarea que se asigna un tiempo determinado
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                /*
                 La tarea a realizar es la presentacion de la pantalla de presentacion  luego
                 cambia al MainActivity
                  */
                Intent iniciarMainAtivity = new Intent(Slash.this,MainActivity.class);

                startActivity(iniciarMainAtivity);
                finish();
            }
        };

        // se define el tiempo de duracion de la presentacion
        Timer tiempoDeDuracionDelTimerTsk = new Timer();

        tiempoDeDuracionDelTimerTsk.schedule(timerTask,4000);
    }



    }

