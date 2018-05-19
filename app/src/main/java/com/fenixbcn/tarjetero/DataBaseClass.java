package com.fenixbcn.tarjetero;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseClass extends SQLiteOpenHelper {



    public DataBaseClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sqlCreate = "CREATE TABLE tags (id_tag INTEGER PRIMARY KEY AUTOINCREMENT, nombre_tag TEXT, color_tag TEXT)";
        sqLiteDatabase.execSQL(sqlCreate); // si no existe la bd la crea

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // esta funcion se ejecuta en el momento en el las versions no la base de datos no son iguales.
        // se puede tanto borrar las tablas y crear unas nuevas con la modificaciones integradas como
        // crear las secuencias sql ALTER TABLE y actualizar las tablas necesarias.

    }
}
