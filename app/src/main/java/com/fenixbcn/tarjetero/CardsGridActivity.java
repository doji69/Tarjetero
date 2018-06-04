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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class CardsGridActivity extends AppCompatActivity {

    private static final String TAG = "CardsGridActivity";
    int tagItemId;
    String tagItemName;
    GridView gvPhotos;
    //ArrayList<String> alTagsSelected = new ArrayList<>();
    ArrayList<File> alTagsSelectedFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_grid);

        // recuperamos las variables pasadas en el Intent

        final Bundle cardsGridActivityVars = getIntent().getExtras();
        tagItemId = cardsGridActivityVars.getInt("tagItemId", -1);
        tagItemName = cardsGridActivityVars.getString("tagItemName");

        // fin recuperamos las variables pasadas en el Intent

        // iniciamos la gridview y recuperamos la lista de archivos del tag selecionado
        gvPhotos = (GridView) findViewById(R.id.gvPhotos);
        CardsGridAdapter cardsGridAdapter;

        getFilesList (); // esta funcion recupera de la base de datos la ruta de las fotos que estan en el tag clicado y lo guarda en un arraylist de files
        //Log.d(TAG, "lista de fotos "+ alTagsSelectedFiles);
        TextView tvCardsGridTitle = (TextView) findViewById(R.id.tvCardsGridTitle);
        tvCardsGridTitle.setText(tagItemName);

        cardsGridAdapter = new CardsGridAdapter(this, alTagsSelectedFiles);
        gvPhotos.setAdapter(cardsGridAdapter);

        // fin iniciamos la gridview y recuperamos la lista de archivos del tag selecionado

        registerForContextMenu(gvPhotos); // resgistra in context menu a la lista de tags

        gvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                File cardItemId = alTagsSelectedFiles.get(i); // obtiene la ruta del tag clicado

                Intent cardsViewActivityVars = new Intent(getApplication(), CardViewActivity.class);
                cardsViewActivityVars.putExtra("card name", cardItemId.toString() );
                cardsViewActivityVars.putExtra("tagItemId", tagItemId);
                startActivity(cardsViewActivityVars);

            }
        });

        // control boton de cancel

        ImageButton btnCancelCard = (ImageButton) findViewById(R.id.btnCancelCard);
        btnCancelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent MainActivityVars = new Intent(getApplication(), MainActivity.class);
                startActivity(MainActivityVars);

            }
        });
        // fin control boton de cancel
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
        cmTags.add(0, v.getId(),0, "Modificar");
        cmTags.add(0, v.getId(),0, "Eliminar");
        cmTags.add(0, v.getId(),0, "Cancelar");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int cardItemIdSel = (int)info.position; // obtiene la posicion del listitem
        final File cardItemId = alTagsSelectedFiles.get(cardItemIdSel); // obtiene la ruta del tag clicado

        if (item.getTitle()=="Modificar") {

            Intent cardsModifyActivityVars = new Intent(getApplication(), CardModifyActivity.class);
            cardsModifyActivityVars.putExtra("card name", cardItemId.toString() );
            cardsModifyActivityVars.putExtra("tagItemId", tagItemId);
            cardsModifyActivityVars.putExtra("cardItemIdSel", cardItemIdSel);
            startActivity(cardsModifyActivityVars);

        } else if (item.getTitle()=="Eliminar") {

            //Toast.makeText(CardsGridActivity.this, "Delete card " + cardItemId, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder adDeleteBuilder = new AlertDialog.Builder(this);
            adDeleteBuilder.setTitle("Borrado de Cards");
            adDeleteBuilder.setMessage("Desea borrar este Card?");
            adDeleteBuilder.setPositiveButton("si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SQLiteDatabase db = Functions.accessToDb(CardsGridActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
                    String sqlDelete = "DELETE FROM cards WHERE nombre_card='" + cardItemId + "'";
                    db.execSQL(sqlDelete);
                    db.close();
                    cardItemId.delete();

                    Toast.makeText(CardsGridActivity.this, "Delete correcto",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());

                }
            });

            adDeleteBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();

                }
            });

            AlertDialog adDelete = adDeleteBuilder.create();
            adDelete.show();

        }

        return true;
    }

    // fin menu contextual al mantener clicado cada uno de los items de la lista

    @Override
    public void onBackPressed() { // anula el back button del despositivo

    }
}
