package com.example.antonio.gesapp;



import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.antonio.gesapp.Helpers.DBHelper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button botonRegistroDeConductor, botonLoginDeConductor,
            botonVehiculosRegistrados,botonInicioDeSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setLogo(R.mipmap.home);

        // castin de los objetos no se utiliza la palabra new por que no se esta reando el objeto

        botonRegistroDeConductor = (Button) findViewById(R.id.botonRegistroDeCoductorXML);
        botonLoginDeConductor = (Button)findViewById(R.id.botonLoginDeConductorXML);
        botonVehiculosRegistrados = (Button)findViewById(R.id.botonVehiculosRegistradosXML);
        botonInicioDeSesion = (Button)findViewById(R.id.botonNuevoInicioDeCesionXML);

        botonRegistroDeConductor.setOnClickListener(this);
        botonLoginDeConductor.setOnClickListener(this);
        botonVehiculosRegistrados.setOnClickListener(this);
        botonInicioDeSesion.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){



            case R.id.botonRegistroDeCoductorXML:

                Intent intentRegistro = new Intent(MainActivity.this, RegistroConductorActivity.class);
                startActivity(intentRegistro);
                finish();


                break;

            case R.id.botonLoginDeConductorXML:
                Intent intentLogin = new Intent(getApplicationContext(),LoginDeConductorActivity.class);
                startActivity(intentLogin);
                finish();
                break;

            case R.id.botonVehiculosRegistradosXML:
                Intent intentLista = new Intent(getApplicationContext(),ListaDeVehiculosRegistradosActivity.class);
                startActivity(intentLista);
                finish();
                break;

            case R.id.botonNuevoInicioDeCesionXML:
                AlertDialog.Builder alertaDeInicioDeCesion = new AlertDialog.Builder(MainActivity.this);
                alertaDeInicioDeCesion.setTitle("Advertencia!");
                alertaDeInicioDeCesion.setIcon(R.drawable.ic_warning_primary_30dp);
                alertaDeInicioDeCesion.setMessage("Al confirmar un nuevo inicio de sesión, el sistema borrara todos los registros de los vehículos que hasta el momento a realizado.\n\n¿Está seguro de querer continuar?");
                alertaDeInicioDeCesion.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // creamos una intancia para usar los metodos
                        DBHelper dbHelper = new DBHelper(getApplicationContext());

                        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

                        String guardaNombreDeLaTablaQueSeQuiereBorrar = "placas";

                        //borramos la tabla de nombre placa
                        sqLiteDatabase.delete(guardaNombreDeLaTablaQueSeQuiereBorrar,null,null);

                        Toast.makeText(getApplicationContext(), "Se a iniciado una nueva cesión.", Toast.LENGTH_LONG).show();
                    }
                });

                alertaDeInicioDeCesion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getApplicationContext(), "Operación cancelada.", Toast.LENGTH_LONG).show();
                    }
                });

                alertaDeInicioDeCesion.show();



                break;


        }

    }


}
