package com.example.antonio.gesapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.antonio.gesapp.Pojos.Conductor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


// Definimos la base de datos con su constructor y sus 2 metodos
public class DBHelper extends SQLiteOpenHelper {

    private static  final  String NOMBRE_DB = "DBconductores";
    private static  final  int VERSION_DB = 1;

    // Definicion de la primera tabla lleva las columnas de nombre: placa y contraseña
    public static  final  String TABLA_PLACAS = "CREATE TABLE placas (Placa text, Contraseña text)";

    // Definicion de la segunda tabla lleva las columna de nombre: placa
    public static  final  String TABLA_VERIFICAR_REGISTRO = "CREATE TABLE consulta (Placa text)";


    public DBHelper(@Nullable Context context) {
        super(context, NOMBRE_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //TABLA 1
        sqLiteDatabase.execSQL(TABLA_PLACAS);
        // TABLA 2
        sqLiteDatabase.execSQL(TABLA_VERIFICAR_REGISTRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        //sqLiteDatabase.execSQL("DROP TABLE  placas");
        //sqLiteDatabase.execSQL(TABLA_PLACAS);
    }

    public boolean consultaSiExisteLatabla(String nombreTabla) {
        boolean isExist = false;

        // este cursor busca si existe la tabla de nobre placas
        Cursor cursor = this.getReadableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + nombreTabla + "'", null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                isExist = true;
                // se pone a la base de datos en modo lectura
                SQLiteDatabase sqLiteDatabase = getReadableDatabase();
                // borramos a la tabla de nombre placas
                sqLiteDatabase.delete("placas",null,null);
            }
            cursor.close();
        }
        return isExist;
    }

    // METODO PARA INGRESAR LOS DATOS A LA BASE DE DATOS, PLACA Y CONTRASEÑA//

    public void incertarPlacaYContraseña(String placa, String contraseña){

        ContentValues contentValues = new ContentValues(2);
        contentValues.put("Placa", placa);
        contentValues.put("Contraseña",contraseña);
        // los parametros son el nombre de la tabla, null y el contenedor donde se almacenarosn los registros
        this.getWritableDatabase().insert("placas",null,contentValues);

    }

    // METODO PARA INGRESAR EL DATO A LA BASE DE DATOS, SOLO LA PLACA //
    public void incertarSoloPlaca(String placa){

        ContentValues contentValues = new ContentValues(1);
        contentValues.put("Placa", placa);
        // los parametros son el nombre de la tabla, null y el contenedor donde se almacenarosn los registros
        this.getWritableDatabase().insert("consulta",null,contentValues);

    }


    public List<Conductor> mostrarListaConductores() {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        // creamos cursor               Del objeto sqLiteDatabase utilizamos el metodo rawQuery el cual contendra la peticion sql
        Cursor cursor =sqLiteDatabase.rawQuery("SELECT * FROM placas ", null);
        // creamos una lista basada en nuestro modelo
        List<Conductor> nuevaListaQueAlmacenaConductores = new ArrayList<>();

        // Se crea una condisional para verifiar si la base de datos si existe almenos algun registro
        if (cursor.moveToFirst()) {

            // bucle do whait para hecer que el curso rrecor a la base de datos y este asu vez balla
            do {
// agregamos todos los registros que se ballan encontrando
                nuevaListaQueAlmacenaConductores.add(new Conductor(cursor.getString(0), cursor.getString(1)));

            } while (cursor.moveToNext());

        }
        return nuevaListaQueAlmacenaConductores;
    }

    // metodo que permite abrir la base de datos

    public void abrirDB(){

        this.getWritableDatabase();
    }

    // cerrar la bse de daros

    public void cerrarDB(){
        this.close();
    }

/////////////// METODO PARA CONTAR REGISTROS E LA BASE DE DATOS//////////////////////////////

    public int contador() {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM placas ";
        int resultado = sqLiteDatabase.rawQuery(sql, null).getCount();
        sqLiteDatabase.close();
        return resultado;
    }

    // Metodo para consultar si la placa existe
    public Cursor consultarSiExisteLaPlaca(String placa)throws SQLException {

        Cursor cursor = null;
        cursor = this.getReadableDatabase().query("placas",new String[]{"Placa"},"Placa like '"+placa+"'",null,null,null,null );
        // Si la placa existe regresara un cursor
        return cursor;
    }
    // Metodo para consultar si existe la placa y la contraseña

    public Cursor consultarSiExisteLaPlacaYLaContraseña(String placa, String contraseña)throws SQLException {

        Cursor cursor = null;
        cursor = this.getReadableDatabase().query("placas",new String[]{"Placa","Contraseña"},"Placa like '"+placa+"' and Contraseña like '"+contraseña+"'", null,null,null,null,null);
        // Si la placa existe regresara un cursor
        return cursor;
    }

    public boolean compruebaSiExisteLaBaseDeDatos(String Database_path) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(Database_path, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.e("Error", "No existe la base de datos " + e.getMessage());
        }
        return checkDB != null;
    }


    // Metodo para consultar si la placa ya se consulto 2 veces
    public Cursor consultarSiYaSeVerificoDosVecesLaPlaca(String placa)throws SQLException {

        Cursor cursor = null;
        cursor = this.getReadableDatabase().query("consulta",new String[]{"Placa"},"Placa like '"+placa+"'",null,null,null,null );
        // Si la placa existe regresara un cursor
        return cursor;
    }




}
