package com.example.antonio.gesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gesapp.Helpers.DBHelper;
import com.example.antonio.gesapp.Pojos.Conductor;
import com.example.antonio.gesapp.adapter.ListaDeVehiculosAdapter;

import java.sql.SQLException;
import java.util.List;

public class RegistroConductorActivity extends AppCompatActivity {


    private TextInputEditText inputEditTextGuardaContraseña, inputEditTextGuarPlaca, inputEditTextGuardarRepetirContrasena;
    private Cursor cursorConsultaSiExistePlaca;
    private Button buttonResgurdoDeVehiculos;
    private  TextView mostarPlacaRepetida;
    private String guardarPlacaRepetida;
    int contador;


    // guarda el numero de placa encontrado para ser mostrado en el ms tohas
    String guardarplaca;
    // persistencia de datos
    private SharedPreferences guardarDatoContador;;

    // se crea una instancia de donde se definio el metodo de incertar los datos para poder usar sus metodos
    DBHelper dbHelper = new DBHelper(this);

    ////////////////////////////////cierre declaracionde de variables y objetos/////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_conductor);

        //llamamos al metodo creado para que nos cuente los registros en el interior de la base de datos


        contadorDePlacas();


        // Bottombar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

////////////////////////////////////////Elementos a los que se les aplica casting/////////////////////

        // Button casting
        buttonResgurdoDeVehiculos = (Button) findViewById(R.id.botonResguardarVehicculo);

        // InputEdiText casting
        inputEditTextGuarPlaca = (TextInputEditText) findViewById(R.id.getplacas);
        inputEditTextGuardaContraseña = (TextInputEditText) findViewById(R.id.getcontraseña);
        inputEditTextGuardarRepetirContrasena = (TextInputEditText) findViewById(R.id.getconfirmacontraseña);
        mostarPlacaRepetida = (TextView) findViewById(R.id.textViewPlacaRepetidaXML);

/////////////////////////////////////////////cierre contenedor de elementos casting/////////////////

        ///////////////////////// Ingresar registros (placa y contraseña)//////////////////

        buttonResgurdoDeVehiculos.setOnClickListener(new View.OnClickListener() {

            // Evento onclik se le asigna el metodo para realizar registros a la base de datos
            @Override
            public void onClick(View view) {

                //***************** se inicializa el contador para saber las veces que da click en el boton
              //  contador = contador + 1;


                //******************* condicional para limitar registros   NOTA hay que declarar la ersistencia de datos del oontador para habilitar esta funcion

                if (contador >=8){


                  //  Toast.makeText(getApplicationContext(), "Esta verción esta limitada en su uso, obtenga la verción premium en la tienda de PlayStore ", Toast.LENGTH_LONG).show();


                }else{

                    String placa = inputEditTextGuarPlaca.getText().toString();
                    String contrasena = inputEditTextGuardaContraseña.getText().toString();
                    String repetirContrasena = inputEditTextGuardarRepetirContrasena.getText().toString();

                    int obtenerTamañoNumeroDePlaca = placa.length();
                    int obtenerTamañoNumeroDeContraseña = contrasena.length();
                    //  int obtenerRepetirNumeroDeContrseña = repetirContrasena.length();



                    if (!placa.isEmpty() && !contrasena.isEmpty() && !repetirContrasena.isEmpty() && contrasena.equals(repetirContrasena)) {


                        /////****** codigo corregido
                        if (obtenerTamañoNumeroDeContraseña == 2 && obtenerTamañoNumeroDePlaca ==5 || obtenerTamañoNumeroDeContraseña == 2 && obtenerTamañoNumeroDePlaca ==6 || obtenerTamañoNumeroDeContraseña ==2 && obtenerTamañoNumeroDePlaca ==7  ) {

                      /*
                           Comprovamos si la base de datos existe si es verdad buscamos el registro que se intenta
                           introducir mediante el metodo: consultarSiExisteLaPlaca creado en DBHelper
                       */
                            String paht = "/data/data/com.example.antonio.usodegridview/databases/dbConductores.db";
                            // llamamos el metodo para que comprueve la existencia de los datos
                            dbHelper.compruebaSiExisteLaBaseDeDatos(paht);

                            if (paht != null){
                                // si existe es diferente de null es decir retorna un 1 valor booleano del metodo y entra al siguiente bucle

                                try {

                                    // Al existir la base de datos
                                    cursorConsultaSiExistePlaca = dbHelper.consultarSiExisteLaPlaca(inputEditTextGuarPlaca.getText().toString());



                                    if (cursorConsultaSiExistePlaca.getCount()==1){


                                        // guardar placa repetida

                                        guardarPlacaRepetida = inputEditTextGuarPlaca.getText().toString();

                                        // asignamos la placa repetida al Texview

                                        mostarPlacaRepetida.setText(guardarPlacaRepetida);

                                        // sentencia que muestra alerta de placa repetida esta vercion la tiene desavilitada comentado el codigo

                                        Toast.makeText(getApplicationContext(), "No se permite registrar 2 veces la misma placa.", Toast.LENGTH_SHORT).show();




                                        dbHelper.cerrarDB();

                                    }else{



                                        dbHelper.abrirDB();
                                        dbHelper.getWritableDatabase();


                                        dbHelper.incertarPlacaYContraseña(String.valueOf(inputEditTextGuarPlaca.getText()),String.valueOf(inputEditTextGuardaContraseña.getText()));

                                        // guardamos placa para mostrar en el MS-tohas
                                        guardarplaca = inputEditTextGuarPlaca.getText().toString();

                                        dbHelper.cerrarDB();

                                        Toast.makeText(getApplicationContext(), "Vehículo protegido placa: " + guardarplaca, Toast.LENGTH_LONG).show();
                                        // limpiamos los inputs
                                        inputEditTextGuarPlaca.setText("");
                                        inputEditTextGuardaContraseña.setText("");
                                        inputEditTextGuardarRepetirContrasena.setText("");



                                        (RegistroConductorActivity.this).contadorDePlacas();


                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }

                            // validasion de parametros en caso de ingresar la contraseña y la placa de manera incorrecta

                        }


                        /////****** codigo corregido
                        else if (obtenerTamañoNumeroDeContraseña > 2 && obtenerTamañoNumeroDePlaca ==5 || obtenerTamañoNumeroDeContraseña > 2 && obtenerTamañoNumeroDePlaca ==6 || obtenerTamañoNumeroDeContraseña >2 && obtenerTamañoNumeroDePlaca ==7  ) {

                            Toast.makeText(getApplicationContext(), "Solo se permiten 2 caracteres para su contreseña.", Toast.LENGTH_LONG).show();
                        }
                        /////****** codigo corregido
                        else if(obtenerTamañoNumeroDeContraseña > 2 && obtenerTamañoNumeroDePlaca >7) {

                            Toast.makeText(getApplicationContext(), "Los parametros de placa y contraseña son incorrectos.", Toast.LENGTH_LONG).show();

                        }

                        else if (obtenerTamañoNumeroDeContraseña == 1 && obtenerTamañoNumeroDePlaca >0){

                            Toast.makeText(getApplicationContext(), "La contraseña debe de ser de 2 caracteres.", Toast.LENGTH_LONG).show();
                        }else if (obtenerTamañoNumeroDeContraseña <= 2 && obtenerTamañoNumeroDePlaca <=4  || obtenerTamañoNumeroDeContraseña>=3 && obtenerTamañoNumeroDePlaca<=4){

                            Toast.makeText(getApplicationContext(), "Su número de placa y/o contraseña no cumplen con los parámetros requeridos.", Toast.LENGTH_LONG).show();
                        }else if (obtenerTamañoNumeroDeContraseña == 2 && obtenerTamañoNumeroDePlaca >=8){

                            Toast.makeText(getApplicationContext(), "Su número de placa no cumple con el parametro requerido.", Toast.LENGTH_LONG).show();
                        }
                    }

                    else

                    if (placa.equals("") && contrasena.equals("") && repetirContrasena.equals("")) {

                        Toast.makeText(getApplicationContext(), "Ingrese sus datos por favor.", Toast.LENGTH_LONG).show();

                    } else if (contrasena.equals(repetirContrasena) && placa.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Ingrese su número de placa por favor.", Toast.LENGTH_LONG).show();

                    } else if (!placa.isEmpty() && contrasena.isEmpty() && repetirContrasena.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Ingrese sus contraseñas por favor.", Toast.LENGTH_LONG).show();

                    } else if (!placa.isEmpty() && !contrasena.isEmpty() && repetirContrasena.isEmpty()) {

                        Toast.makeText(getApplicationContext(), "Su contraseña tiene que repetirla 2 veces.", Toast.LENGTH_LONG).show();

                    } else if (placa.isEmpty() && !contrasena.isEmpty() && repetirContrasena.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingrese su número de placa y repita su contraseña.", Toast.LENGTH_LONG).show();

                    } else if (placa.equals(placa) && contrasena.equals(contrasena) || repetirContrasena.equals(repetirContrasena)) {
                        Toast.makeText(getApplicationContext(), "Sus 2 contraseñas deben de coincidir.", Toast.LENGTH_LONG).show();

                    }
                }// cierre del else que limita

            }






        });
    }

//////////////////////Cierre del Metodo para ingresar los datos a la base de datos//////////////////////////////////////

    //Metodo para contar registros de la base de datos


    public  void contadorDePlacas(){

        int contadorPlacas = new DBHelper(this).contador();
        TextView textViewTotalDePlacas = findViewById(R.id.totalDePlacasXML);

        //convertir texto a entero
        textViewTotalDePlacas.setText(String.valueOf(contadorPlacas));
    }






    //*************************************persistencia de datos
    public void onPause(){

        // Llamamos al metodo super que nos permite sobre escribir el metodo
        super.onPause();

        //// Se editar preferencia que se mostrara dato persitente, posteriormente en el Texview: mostrarResultadoCorrecto //////

        // creamos un nuevo objeto SharedPreferences el cual se lllamara: guardarDatosConsultadosCorrectamente
        // guardarDatos = getSharedPreferences("archivoDePreferencias", Context.MODE_PRIVATE);

        // creamos un editor para editar las preferencias del SharedPreferences


        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor  mieditor = datos.edit();

        // tomamos los datos

        mieditor.putString("placaRepetida",guardarPlacaRepetida);


        mieditor.apply();
    } /// fin Metodo onPause

    public void onResume(){

        super.onResume();

        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        guardarPlacaRepetida =datos.getString("placaRepetida", "");

        // asignamos el numero de placa repetida
        mostarPlacaRepetida.setText(guardarPlacaRepetida);


    }/// fin Metodo onResume




    ///////// Modulo de configuracion de comunicasion del bottonbar hacia otras actividades/////////////

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.HomeXML:

                    Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentHome);
                    finish();
                    return true;

                case R.id.RegistroDeConductorXML:
                    Intent intentRegistroConductor = new Intent(getApplicationContext(), RegistroConductorActivity.class);
                    startActivity(intentRegistroConductor);
                    finish();
                    return true;

                case R.id.LoginDeConductorXML:

                    Intent intentLogin = new Intent(getApplicationContext(), LoginDeConductorActivity.class);
                    startActivity(intentLogin);
                    finish();
                    return true;

                case R.id.ListaDeVehiculosRegistradosXML:

                    Intent intentLista = new Intent(getApplicationContext(), ListaDeVehiculosRegistradosActivity.class);
                    startActivity(intentLista);
                    finish();
                    return true;
            }
            return false;
        }
    };

}
