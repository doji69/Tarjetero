package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class TagsMainActivity extends AppCompatActivity {

    private ListView lvListaTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_main);

        // control del listado de tags y select que lo rellena

        lvListaTags = (ListView) findViewById(R.id.lvListaTags);
        ArrayList<TagsClass> alTags = new ArrayList<>();
        TagsAdapter tags;

        DataBaseClass dbTajetero = new DataBaseClass(TagsMainActivity.this, "tarjetero", null, 1);
        SQLiteDatabase db = dbTajetero.getWritableDatabase();

        String selectTags = "SELECT * from tags";
        Cursor selectCursor = db.rawQuery(selectTags,null);

        if (selectCursor.moveToFirst()) {

            do {

                alTags.add(new TagsClass(selectCursor.getString(1), selectCursor.getInt(2)));

            } while (selectCursor.moveToNext());
        }

        //alTags.add(new TagsClass("pepe", -6340683));
        //alTags.add(new TagsClass("javito", -11094721));

        tags = new TagsAdapter(this, alTags);

        lvListaTags.setAdapter(tags);

        db.close();

        // fin control del listado de tags y select que lo rellena

        registerForContextMenu(lvListaTags); // resgistra in context menu a la lista de tags

    }

    // Control del menu del action bar
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
                Intent viewTagsViewActivity = new Intent(getApplication(), AddTagActivity.class);
                startActivity(viewTagsViewActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

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
        int tagItemId = (int)info.id;

        if (item.getTitle()=="Modificar") {

            Toast.makeText(TagsMainActivity.this, "modificamos item " + tagItemId, Toast.LENGTH_SHORT).show();
            Intent AddTagActivityVars = new Intent(getApplication(), AddTagActivity.class);
            AddTagActivityVars.putExtra("tagItemId", tagItemId);
            AddTagActivityVars.putExtra("tagAction", "modificar");
            startActivity(AddTagActivityVars);

        } else if (item.getTitle()=="Eliminar") {

            Toast.makeText(TagsMainActivity.this, "eliminamos item " + tagItemId, Toast.LENGTH_SHORT).show();

        }

        return true;
    }

    // fin menu contextual al mantener clicado cada uno de los items de la lista
}
