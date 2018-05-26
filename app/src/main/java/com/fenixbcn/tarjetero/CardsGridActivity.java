package com.fenixbcn.tarjetero;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
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

        registerForContextMenu(gvPhotos); // resgistra in context menu a la lista de tags
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

    // menu contextual al mantener clicado cada uno de los items de la lista

    @Override
    public void onCreateContextMenu(ContextMenu cmTags, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(cmTags, v, menuInfo);

        cmTags.setHeaderTitle("Acciones");
        cmTags.add(0, v.getId(),0, "Ver");
        cmTags.add(0, v.getId(),0, "Modificar");
        cmTags.add(0, v.getId(),0, "Eliminar");
        cmTags.add(0, v.getId(),0, "Cancelar");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int cardItemIdSel = (int)info.position; // obtiene la posicion del listitem
        final File cardItemId = alTagsSelectedFiles.get(cardItemIdSel); // obtiene el id del tag clicado

        if (item.getTitle()=="Ver") {

            Toast.makeText(CardsGridActivity.this, "Show card " + cardItemId, Toast.LENGTH_SHORT).show();


        } else if (item.getTitle()=="Modificar") {

            Toast.makeText(CardsGridActivity.this, "Modify card " + cardItemId, Toast.LENGTH_SHORT).show();


        } else if (item.getTitle()=="Eliminar") {

            Toast.makeText(CardsGridActivity.this, "Delete card " + cardItemId, Toast.LENGTH_SHORT).show();

            /*AlertDialog.Builder adDeleteBuilder = new AlertDialog.Builder(this);
            adDeleteBuilder.setTitle("Borrado de Cards");
            adDeleteBuilder.setMessage("Desea borrar este Card?");
            adDeleteBuilder.setPositiveButton("si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });

            adDeleteBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();

                }
            });

            AlertDialog adDelete = adDeleteBuilder.create();
            adDelete.show();*/

        }

        return true;
    }

    // fin menu contextual al mantener clicado cada uno de los items de la lista

    @Override
    public void onBackPressed() { // anula el back button del despositivo

    }
}
