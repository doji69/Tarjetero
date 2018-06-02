package com.fenixbcn.tarjetero;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TagsMainActivity extends AppCompatActivity {

    private ListView lvListaTags;
    ArrayList<TagsClass> alTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_main);

        // control del listado de tags y select que lo rellena

        lvListaTags = (ListView) findViewById(R.id.lvListaTags);
        alTags = new ArrayList<>();
        TagsAdapter tags;

        SQLiteDatabase db = Functions.accessToDb(TagsMainActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
        String selectTags = "SELECT * from tags";
        Cursor selectCursor = db.rawQuery(selectTags,null);

        if (selectCursor.moveToFirst()) {

            do {

                alTags.add(new TagsClass(selectCursor.getInt(0), selectCursor.getString(1), selectCursor.getInt(2)));

            } while (selectCursor.moveToNext());
        }

        tags = new TagsAdapter(this, alTags);

        lvListaTags.setAdapter(tags);

        db.close();

        // fin control del listado de tags y select que lo rellena

        registerForContextMenu(lvListaTags); // resgistra in context menu a la lista de tags

        // control botton de añadir un tag
        ImageButton btnAddTag = (ImageButton) findViewById(R.id.btnAddTag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent tagsAddActivityVars = new Intent(getApplication(), TagAddActivity.class);
                startActivity(tagsAddActivityVars);

            }
        });
        // fin control botton de añadir un tag

        // control botton de back
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent MainActivityVars = new Intent(getApplication(), MainActivity.class);
                startActivity(MainActivityVars);

            }
        });
        // fin control botton de back

    }

    // Control del menu del action bar
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater loadMenu = getMenuInflater();
        loadMenu.inflate(R.menu.tags_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.iAddTag:
                //Toast.makeText(TagsMainActivity.this, "Add Tag", Toast.LENGTH_SHORT).show();
                Intent tagsAddActivityVars = new Intent(getApplication(), TagAddActivity.class);
                startActivity(tagsAddActivityVars);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    */
    // fin Control del menu del action bar

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
        final int tagItemIdSel = (int)info.position; // obtiene la posicion del listitem
        final int tagItemId = alTags.get(tagItemIdSel).getTagId(); // obtiene el id del tag clicado

        if (item.getTitle()=="Modificar") {

            //Toast.makeText(TagsMainActivity.this, "Modify tag " + tagItemId, Toast.LENGTH_SHORT).show();
            Intent tagModifyActivityVars = new Intent(getApplication(), TagModifyActivity.class);
            tagModifyActivityVars.putExtra("tagItemId", tagItemId);
            //ModifyTagActivityVars.putExtra("tagAction", "modificar");
            startActivity(tagModifyActivityVars);

        } else if (item.getTitle()=="Eliminar") {

            //Toast.makeText(TagsMainActivity.this, "Delete tag " + tagItemId, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder adDeleteBuilder = new AlertDialog.Builder(this);
            adDeleteBuilder.setTitle("Borrado de tags");
            adDeleteBuilder.setMessage("Desea borrar este tag?");
            adDeleteBuilder.setPositiveButton("si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SQLiteDatabase db = Functions.accessToDb(TagsMainActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
                    String sqlDelete = "DELETE FROM tags WHERE id_tag=" + tagItemId;
                    db.execSQL(sqlDelete);
                    db.close();

                    Toast.makeText(TagsMainActivity.this, "Delete correcto",Toast.LENGTH_SHORT).show();

                    Intent TagsMainActivityVars = new Intent(getApplication(), TagsMainActivity.class);
                    startActivity(TagsMainActivityVars);

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
    public void onBackPressed() {

    }
}
