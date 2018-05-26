package com.fenixbcn.tarjetero;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class CardsGridActivity extends AppCompatActivity {

    private static final String TAG = "CardsGridActivity";
    int tagItemId;
    GridView gvPhotos;
    //ArrayList<String> alTagsSelected = new ArrayList<>();
    ArrayList<File> alTagsSelectedFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_grid);

        // recuperamos las variables pasadas en el Intent

        Bundle AddTagActivityVars = getIntent().getExtras();
        tagItemId = AddTagActivityVars.getInt("tagItemId", -1);

        // fin recuperamos las variables pasadas en el Intent

        // iniciamos la gridview y recuperamos la lista de archivos del tag selecionado
        gvPhotos = (GridView) findViewById(R.id.gvPhotos);
        CardsGridAdapter cardsGridAdapter;

        getFilesList (); // esta funcion recupera de la base de datos la ruta de las fotos que estan en el tag clicado y lo guarda en un arraylist de files
        Log.d(TAG, "lista de fotos "+ alTagsSelectedFiles);

        cardsGridAdapter = new CardsGridAdapter(this, alTagsSelectedFiles);
        gvPhotos.setAdapter(cardsGridAdapter);

        // fin iniciamos la gridview y recuperamos la lista de archivos del tag selecionado
    }

    public void getFilesList () {

        SQLiteDatabase db = Functions.accessToDb(CardsGridActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
        String[] select_params = new String[] {String.valueOf(tagItemId)};
        String selectNumCards = "SELECT * from cards where id_tag=?";
        Cursor cursor = db.rawQuery(selectNumCards, select_params);

        if (cursor.moveToFirst()) {

            do {

                alTagsSelectedFiles.add(new File(cursor.getString(1)));

            } while (cursor.moveToNext());
        }

        db.close();


    }
}
