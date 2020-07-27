package com.example.antonio.gesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.antonio.gesapp.Helpers.DBHelper;

import java.sql.SQLException;

public class LoginDeConductorActivity extends AppCompatActivity {

    // cuenta los clik del boton
    private int onclik;

    /*
     variables de consulta y de conteo de clicks del boton se crean estas 2 variables para guardar los datos y \
     mantener persistencia estas se declaran en los metodos onPause y onResume
     */
    private int guardaconsultaPlacaYContrasena;
    private int guardarTotalDeOnclick;

    // variables usadas en el boke de persistencia de datos se usa para guardar el numero de placa ya consultado en el login
    private String guardarNumeroDePlacaError1Persistencia;
    private String guardarNumeroDePlacaError2Persistencia;

    // variable guarda testo y numero de placa ya consultados de forma correcta
    private String guardaPlacaYTextoConsultaCorrecta;

    private TextView mostrarTotalDeRegistrosDeLaBaseDeDatos, mostrarResultadoDeLogin1,mostrarResultadoDeLogin2, mostrarResultadoCorrecto;

    // Capturan el numero deplaca y contraseña del conductor
    private TextInputEditText inputEditTextGuardaContraseña,inputEditTextGuarPlaca;

    /*
     cursor1:Busca si exiete el numero de placa en la base de datos, se utliza para saber si el numero de placa ya esta registrada
     cursor2:Busca  si existe en la base de datos el numero de placa y la contraseña
     cursor3:Busca si ya la placa y la contraseña se consultarin 2 veces sirve para limitar  mas consultas.

     Nota el puntero puede dar solo 2 resultados de la consulta si encuentra el registro dara == 1 si no dara reusltado == 0
     */
    private Cursor cursor1,cursor2, cursor3;


    // variable guarda texto de correcto, esta variable se concatena con la variable que guarda la placa (placaEncontrada)
    private String guardaSoloTextoDeConsultaValida;

    // variables guardan texto de error, esta variable se concatena con la variable que guarda la placa (placaEncontrada)
    private String guardaSoloTextoDeConsultaError1;
    private String guardaSoloTextoDeConsultaError2;

    // variables que guardan los datos que se van a mostrar en el Share
    private String guardaPlacaYTextoConsulta1Error;
    private String guardaPlacaYTextoConsulta2Error;

    // variable que toma el valor de la placa encontrada del cursor
    private String placaEncontrada;


    // variables que guardan la placa consultada y la contraseña no es correcta
    private String guardaPlaca1raConsultaError;
    private String guardaPlaca2daConsultaError;

    private Button botonValidarContraseña;


    // Se crea la instancia para trabajar (poder llamar los metodos )la base de  datos

    DBHelper dbHelper = new DBHelper(this);

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
                    finish();;

                    return true;

                case R.id.ListaDeVehiculosRegistradosXML:

                    Intent intentLista = new Intent(getApplicationContext(),ListaDeVehiculosRegistradosActivity.class);
                    startActivity(intentLista);
                    finish();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_de_conductor);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // llamamos al metodo para iniciar el conteo del total de placas registradas
        contadorPlacas();

        // Castin de los elementos de la vista. vinculando or referencia de id

        botonValidarContraseña = (Button) findViewById(R.id.botonValidarContrasenaXML);

        mostrarTotalDeRegistrosDeLaBaseDeDatos = (TextView) findViewById(R.id.totalDegistrosdePlacasXML);
        mostrarResultadoCorrecto = (TextView)findViewById(R.id.mostrarResultadoCorrectoxml);
        mostrarResultadoDeLogin1 = (TextView) findViewById(R.id.mostrarResultadointento1xml);
        mostrarResultadoDeLogin2 = (TextView) findViewById(R.id.mostrarResultadointento2xml);

        botonValidarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cuenta los clik del boton
                onclik = onclik+1;

                //  castin de los TextInputEditText
                inputEditTextGuarPlaca = (TextInputEditText)findViewById(R.id.inputObtenerPlacaXML);
                inputEditTextGuardaContraseña = (TextInputEditText)findViewById(R.id.inputVerificarContraseñaXML);

                // Se almacena en varible los datos del conducutor y se capturan como string
                String placa = inputEditTextGuarPlaca.getText().toString();
                String contraseña = inputEditTextGuardaContraseña.getText().toString();

                ////PRIMER CONDICIONAL/////////
/* Se verifica si las variables tienen datos o estan vacias, Si la placa es diferente de vacio y contraseñaes
diferente de vacio entrara al cursor para buscar si existe SOLO el numero de placa*/

                if (!placa.isEmpty()&& !contraseña.isEmpty()) {

                    try {
                        cursor3 = dbHelper.consultarSiYaSeVerificoDosVecesLaPlaca(inputEditTextGuarPlaca.getText().toString());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if (cursor3.getCount()==1 ) {
                        Toast.makeText(getApplicationContext(), "Ya no puede seguir validando sus intentos terminados", Toast.LENGTH_LONG).show();

                        onclik=0;
                        //cerre del cursor
                        cursor3.close();

                    }else if (cursor3.getCount()==0){

                        /// lamando al metodo
                        gestionloginConductor();

                        // actualiza la vista en tiempo real del contador de placas
                        (LoginDeConductorActivity.this).contadorPlacas();

                    }

                }// sentencias resolveran en caso de que el usuario no ingrece su numero de placa, o contraseña o ambos

                else if (placa.isEmpty()&& !contraseña.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese su número de placa por favor.", Toast.LENGTH_LONG).show();
                    // se pone contador a cero
                    onclik--;

                    //sentencia resolvera en caso de que el usuario no ingrece contraseña
                }else if (!placa.isEmpty()&& contraseña.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Ingrese su contraseña por favor.", Toast.LENGTH_LONG).show();
                    // se pone contador a cero
                    onclik--;

                    //sentencia resolvera en caso de que el usuario no ingrece ni placa ni contraseña
                }else if (placa.isEmpty()&& contraseña.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Los campos están vacíos, ingrese sus datos por favor.", Toast.LENGTH_LONG).show();
                    // se pone contador a cero
                    onclik--;
                }
            }
        });
    }


    ///motodo que cuenta el numero total de placas se realiza con sentencia SQL ////

    public  void contadorPlacas(){

        // se guarda dato en variable de tipo entero la acion de contar los registros en la base de datos
        int contadorPlacas = new DBHelper(this).contador();

        mostrarTotalDeRegistrosDeLaBaseDeDatos = (TextView)findViewById(R.id.totalDegistrosdePlacasXML);

        // parseo de datos comvertimos un tipo de dato: en otro.
        mostrarTotalDeRegistrosDeLaBaseDeDatos.setText(String.valueOf(contadorPlacas));
    }

    ///////////////FINDE CONTADOR DE PLACAS////////////////////

    //metodo principal que verifica si exiete la placa, la contraseña y placa, gestiona y manda a pantalla los 2 logueos erroneos asi como el correcto

    /*
    Metodo Principal dentro de este trabajan varios Sub-Metodos
     */
    public void gestionloginConductor(){

        // Sud-metodo verifica si existe el numero de placa
        verificarPlaca();

        //Se la evalua la concional por ○ondicional el resultado que arroja el Sub-Metodo verificar placa() se verifica si existe el numero de placa registrado por primera vez
        if (cursor1.getCount()==1 ) {

            /*
            Este tohas se usa como prueba para corroborar si encuentra o si existe la placa en la base de datos
              Toast.makeText(getApplicationContext(), "Placa encontrada", Toast.LENGTH_LONG).show();
             */

            //cerre del cursor
            cursor1.close();

            // Sud-metodo para verificar si existre el numero de placa y contraseña
            verificarPlacaYContraseña();

//**************************************************************************************************************************
            /*
            Se guardan en variables los valor en○ontrado de la consulta del metodo verificarPlacaYContraseña y el conteo de los Clicks
             */
            // se guarda el resultada de la consulta hecha a la base de datos por medio del puntero
            guardaconsultaPlacaYContrasena = cursor2.getCount();
            // guarda los clik
            guardarTotalDeOnclick = onclik;

            // se evalua en funcion del resultado del  Sub-Metodo verificar placa()
            // bloke de consulta correcta
            // cursor busca placa y contraseña cuando si las encuentra ambas entra al if (guardarResultadoDeBorrarPlaca == 1) de no encontrarla pasa a letra: A
            if (guardaconsultaPlacaYContrasena==1){

                String borrarPlaca =inputEditTextGuarPlaca.getText().toString();
                cursor2.close();

                // **** se pone la base de datos en modo escritura
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

                int guardarResultadoDeBorrarPlaca = sqLiteDatabase.delete("placas", "Placa='" + borrarPlaca + "'", null);

                // cierre del segundo metodo deninido en DBHelper se llama:consultarSiExisteLaPlacaYLaContraseña
                dbHelper.cerrarDB();

                // sentencia verifica si se borro el n umero de placa
                if (guardarResultadoDeBorrarPlaca == 1) {

                    Toast.makeText(getApplicationContext(), "Contraseña correcta.", Toast.LENGTH_LONG).show();

                    // Si la placa es borrada mostrar en el TexView

                    // Variable que guarda texto
                    guardaSoloTextoDeConsultaValida = "Del número de placa: ";

                    // Se  muestra en un TexView (mostrarResultadoDeLogin1)los datos:
                    // que asignamos: el texto y la placa encontrada en el cursor
                    mostrarResultadoCorrecto.setText( guardaSoloTextoDeConsultaValida + placaEncontrada);

                    // guardamos en la variable los datos de la validacion correcta esta variable se usa en el bloke de persistencia de datos

                    guardaPlacaYTextoConsultaCorrecta = mostrarResultadoCorrecto.getText().toString();

                    inputEditTextGuarPlaca.setText("");
                    inputEditTextGuardaContraseña.setText("");

                    // se pone el contador de los clik en cero para iniciar un nuevo conteo y dar oportunidad a que inicie el conteo en los errores
                    onclik=0;

                }

                  }



            // bloke de consultas incorrectas

            if (guardaconsultaPlacaYContrasena==0 ){

                if (onclik==1)
                {
                    primerIntentoIncorrecto();

                }

                else if (guardarTotalDeOnclick>=2){

                   segundoIntentoIncorrecto();

                }
            }

        }

        else {
                // si no existe el numero de placa entra a en este bloke y ejecutara el tohas
            Toast.makeText(getApplicationContext(), "El número de placa no existe", Toast.LENGTH_LONG).show();

            // poner contador a cero para volver a nicializar el conteo
            onclik=0;
        }

    }


    /*
    Sub-Metodos administran el control de la validacion del conductor
     */
    // Metodo que verifica si elnumerom de placa existe en la base de datos
    public void verificarPlaca(){


        // Este cursor busca si existe el numero de placa si el curso encentra algun resultado arroja valor 1
        try {
            cursor1 = dbHelper.consultarSiExisteLaPlaca(inputEditTextGuarPlaca.getText().toString());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // verifica la placa y la contraseña
    public void verificarPlacaYContraseña(){

        // segundo cursor busca nuevamente el numero de placa y contraseña que ingresa el usuario de tener en la base de datos ambas regresara cursor2.getCount()==1 y entra en el siguiente bloke
        try {
            cursor2 = dbHelper.consultarSiExisteLaPlacaYLaContraseña(inputEditTextGuarPlaca.getText().toString(), inputEditTextGuardaContraseña.getText().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // se guarda en una variable:placaEncontrada,  el dato ingresado por teclado por el usuario en el inputEditTextGuarPlaca correspondiente a la placa
        placaEncontrada = inputEditTextGuarPlaca.getText().toString();

    }


    // Par de Metodos en caso de que la validasion sea incorrecta primero ysegundo intento

    public void primerIntentoIncorrecto(){
        // se declara la variable de forma global, guarda texto a mostrar
        guardaSoloTextoDeConsultaError1 = "INCORRECTA del número de placa: ";

        // Esta sentencia muestra los datos en el TexView (pantalla del primer loguin)
        mostrarResultadoDeLogin1.setText( guardaSoloTextoDeConsultaError1 + placaEncontrada);

        //guarda de la primera consulta erronea, la placa consultada
        guardaPlaca1raConsultaError = placaEncontrada;

        // Esta sentencia tomamos los datos que vamos a guardar en el SharedPreferences
        guardaPlacaYTextoConsulta1Error = mostrarResultadoDeLogin1.getText().toString();


        Toast.makeText(getApplicationContext(), "¡Alerta! Primer aviso el vehículo puede estar en riesgo.", Toast.LENGTH_SHORT).show();

    }

    public void segundoIntentoIncorrecto(){

        // Esta sentencia muestra los datos en el TexView

        guardaSoloTextoDeConsultaError2 = "INCORRECTA del número de placa: ";
        mostrarResultadoDeLogin2.setText(guardaSoloTextoDeConsultaError2 + placaEncontrada);

        guardaPlacaYTextoConsulta2Error = mostrarResultadoDeLogin2.getText().toString();

        // se abre la baswe de datos en modo escritura
        dbHelper.abrirDB();
        dbHelper.getWritableDatabase();
        // se incerta le numero de placa que ya fue consultada 2 veces de manera erronea a la base de datos
        // se realizar parse: se conbierte valor a string
        dbHelper.incertarSoloPlaca(String.valueOf(inputEditTextGuarPlaca.getText()));

        dbHelper.cerrarDB();

        Toast.makeText(getApplicationContext(), "¡Alerta! Segundo aviso el veículo puede estar en riesgo.", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Terminaron sus intentos.", Toast.LENGTH_SHORT).show();

        //
        onclik=0;

    }




    //// INICIO DE BLOKE DE PRESISTENCIA DE DATOS ////

//Persistencia de datos atravez de SharedPreferences aplicado en el metodo onPause guardamos los datos que queremos que sean
//persistentes y se sigan mostrando en los 2 TexView ()

    public void onPause(){

        // Llamamos al metodo super que nos permite sobre escribir el metodo
        super.onPause();

        //// Se editar preferencia que se mostrara dato persitente, posteriormente en el Texview: mostrarResultadoCorrecto //////
        SharedPreferences  guardarDatos = getSharedPreferences("archivoDePreferencias", Context.MODE_PRIVATE);
        // creamos un editor para editar las preferencias del SharedPreferences

        SharedPreferences.Editor miEditor1 = guardarDatos.edit();

        // Asignamos los datos a escribir o guardar en el SharedPreferences
        //Con el objeto mieditor1 llamamos el metodo .putString para guardar nuestros datos que se quieren sean persistentes, este metodo
        //lleva 2 parametros uno es una clave (String) la cual podemos llamar como nos paresca adecuado, la otra los valores a guardar.


        // los datos tomados a guardar los tomamos del modulo //Cuando la validacion es correcta// variable:
        miEditor1.putString("datos1", guardaPlacaYTextoConsultaCorrecta);


        miEditor1.putString("datos2",guardaPlacaYTextoConsulta1Error);

        // guarada puro numero de placa de la primera consulta para mantenerlo persistente
        miEditor1.putString("datoplacaerror1",guardaPlaca1raConsultaError);


        miEditor1.putString("datos3",guardaPlacaYTextoConsulta2Error);

        // guarada puro numero de placa de la segunda consulta para mantenerlo persistente
        miEditor1.putString("datoplacaerror2",guardaPlaca2daConsultaError);

        // guardar total de onclicks despues de la primera consulta erronea
        miEditor1.putInt("guardarTotalDeOnclick",guardarTotalDeOnclick );

        // guardar total de onclicks despues de la primera consulta erronea
        miEditor1.putInt("onclik",onclik);




        miEditor1.commit();




    } /// fin Metodo onPause

    public void onResume(){

        super.onResume();

        ///////////////------llamamos al archivo donde se gurdaron las preferencias: guardarDatos--------------//////////
        // Para 1 obtener los datos y
        // 2 mostrarlos en el TexView

        SharedPreferences guardarDatos = getSharedPreferences("archivoDePreferencias", Context.MODE_PRIVATE);

        // 1.- obtenemos los datos guardados en el archivo de preferencias
        guardaPlacaYTextoConsultaCorrecta = guardarDatos.getString("datos1", "");

        // 2.- mostramos los datos recuperados en el texView
        mostrarResultadoCorrecto.setText(guardaPlacaYTextoConsultaCorrecta);




/////////-------------------------------------------------------------------------------------------/////////////////////////
        // 1.- obtenemos los datos guardados en el archivo de preferencias para ser mostrados
        guardaPlacaYTextoConsulta1Error = guardarDatos.getString("datos2", "");

        // 2.- mostramos los datos guardados
        mostrarResultadoDeLogin1.setText(guardaPlacaYTextoConsulta1Error);

        // guardamos en una variable el dato para que persista a pesar de cambiar de pantalla
        guardarNumeroDePlacaError1Persistencia = guardarDatos.getString("datoplacaerror1","");



/////////-------------------------------------------------------------------------------------------/////////////////////
        //  1.- obtenemos los datos guardados en el archivo de preferencias
        guardaPlacaYTextoConsulta2Error = guardarDatos.getString("datos3", "");

        // guardamos en una variable el dato para que persista a pesar de cambiar de pantalla
        guardarNumeroDePlacaError2Persistencia = guardarDatos.getString("datoplacaerror1","");

        // 2.- mostramos los datos guardados asignamos al texview los datos de persistencia
        mostrarResultadoDeLogin2.setText(guardaPlacaYTextoConsulta2Error);

////////--------------------------------------------------------------------------------------------////////////////////

        // obtenemos el valor almacenado en la variable  guardarTotalDeOnclick
        guardarTotalDeOnclick = guardarDatos.getInt(" guardarTotalDeOnclick",0);

        // obtenemos el valor de los onclick
        onclik = guardarDatos.getInt("onclik",0);



    }/// fin Metodo onResume


    //////////// FIN BLOKE PERSISTENCIA /////////////////////////////////////
}
