package com.example.antonio.gesapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.example.antonio.gesapp.Helpers.DBHelper;
import com.example.antonio.gesapp.Pojos.Conductor;
import com.example.antonio.gesapp.adapter.ListaDeVehiculosAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListaDeVehiculosRegistradosActivity extends AppCompatActivity{



    private FloatingActionButton floatingActionButtonRegistro, floatingActionButtonLogin,  floatingActionButtonMenu;

    //definimos recycler view y lo configuramos
    RecyclerView recyclerViewListaDeVehiculos;

// creamos una lista List de un tipo de objeto este se difinio en la carpeta POJOS y es la
// clase Conductor y la llamamos conductorList NOTA: para en filter este sera igual a un nuevo array de datos para el filter. new ArrayList<>()
    //List<Conductor> conductorList = new ArrayList<>();

    // creamos el adapatdor una ves ya credada la clase
    ListaDeVehiculosAdapter listaDeVehiculosAdapter;  // adapter
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_vehiculos_registrados);







//////////////////////////////////////////// CASTIN DE ELELEMNETOS/////////////////////////////////////////////////////////

        // castin de RecyclerView
        recyclerViewListaDeVehiculos = (RecyclerView)findViewById(R.id.listaDeVehiculosRecyclerViewXML);

///////////////////////////// cierre de CASTIN DE ELELEMNETOS///////////////////////////////////////////////////

        recyclerViewListaDeVehiculos.setHasFixedSize(true);

        DBHelper dbHelper = new DBHelper(this);

        // Creamo el LinearLayoutManager para definir la orientasion de la vista del RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);

        // COLOCAMOS EL LinearLayoutManager AL RecyclerView se lo pasamos en el parametro
        recyclerViewListaDeVehiculos.setLayoutManager(linearLayoutManager);

        // En nustro adapter
        //Sustituimos el nombre del metodo del Arreglo: obtenerconductor()
        //Para ingresar el metodo que cargara los datos desde la base de datos nel merodo se llama: mostrarListaConductores()
        listaDeVehiculosAdapter = new ListaDeVehiculosAdapter(this,dbHelper.mostrarListaConductores());
        recyclerViewListaDeVehiculos.setAdapter(listaDeVehiculosAdapter);




        floatingActionButtonRegistro = (FloatingActionButton)findViewById(R.id.botonRegistro);
        floatingActionButtonLogin = (FloatingActionButton)findViewById(R.id.botonLogin);
        floatingActionButtonMenu = (FloatingActionButton)findViewById(R.id.botonMenu);


        floatingActionButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(ListaDeVehiculosRegistradosActivity.this, RegistroConductorActivity.class);
                startActivity(intentRegistro);
                finish();
            }
        });

        floatingActionButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(ListaDeVehiculosRegistradosActivity.this, LoginDeConductorActivity.class);
                startActivity(intentRegistro);
                finish();
            }
        });


        floatingActionButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegistro = new Intent(ListaDeVehiculosRegistradosActivity.this, MainActivity.class);
                startActivity(intentRegistro);
                finish();
            }
        });

    }








    /*
     Metodo que crea una lista, se agregan las placas para realizar testin y saber que esta
     bien definido el adapter
      */

    /*  public List<Conductor> obtenerconductor(){
          List<Conductor>conductorList = new ArrayList<>();
          conductorList.add(new Conductor("908-HTY","4r"));
          conductorList.add(new Conductor("345-POI","4r"));
          conductorList.add(new Conductor("678-POI","4r"));
          conductorList.add(new Conductor("001-POI","4r"));
          conductorList.add(new Conductor("567-POI","4r"));
          conductorList.add(new Conductor("123-POI","4r"));
          return  conductorList;
      }
  */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.buscarXML);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listaDeVehiculosAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }





}





