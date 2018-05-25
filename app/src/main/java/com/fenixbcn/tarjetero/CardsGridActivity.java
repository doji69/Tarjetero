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
    ArrayList<String> alTagsSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_grid);

        Bundle AddTagActivityVars = getIntent().getExtras();
        tagItemId = AddTagActivityVars.getInt("tagItemId", -1);

        Toast.makeText(CardsGridActivity.this, "el id pasado es " + tagItemId, Toast.LENGTH_SHORT).show();

        gvPhotos = (GridView) findViewById(R.id.gvPhotos);

        getFilesList ();
        Log.d(TAG, "lista de fotos "+ alTagsSelected );

        ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alTagsSelected);
        gvPhotos.setAdapter(arrayListArrayAdapter);


    }

    public void getFilesList () {

        SQLiteDatabase db = Functions.accessToDb(CardsGridActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
        String[] select_params = new String[] {String.valueOf(tagItemId)};
        String selectNumCards = "SELECT * from cards where id_tag=?";
        Cursor cursor = db.rawQuery(selectNumCards, select_params);

        if (cursor.moveToFirst()) {

            do {

                alTagsSelected.add(cursor.getString(1));

            } while (cursor.moveToNext());
        }

        db.close();


    }
}
