package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvMainListaTags;
    ArrayList<TagsClass> alMainTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDataBase(); // arrancamos la base de datos

        // control y mostrado de la lista en el main principal donde poder ver los tags y cuantas cards hay en cada tag

        lvMainListaTags = (ListView) findViewById(R.id.lvMainListaTags);
        alMainTags = new ArrayList<>();
        TagsAdapter mainTags;

        SQLiteDatabase db = Functions.accessToDb(MainActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
        String selectMainTags = "SELECT * from tags";
        Cursor selectCursor = db.rawQuery(selectMainTags,null);

        if (selectCursor.moveToFirst()) {

            do {

                alMainTags.add(new TagsClass(selectCursor.getInt(0), selectCursor.getString(1), selectCursor.getInt(2)));

            } while (selectCursor.moveToNext());
        }

        mainTags = new TagsAdapter(this, alMainTags);
        lvMainListaTags.setAdapter(mainTags);

        // fin control y mostrado de la lista en el main principal donde poder ver los tags y cuantas cards hay en cada tag

        lvMainListaTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // activity que mostrara en una grid los cards de esta tag
                //Toast.makeText(MainActivity.this, "" + alMainTags.get(i).getTagId(), Toast.LENGTH_LONG).show();

                Intent cardsGridActivityVars = new Intent(getApplication(), CardsGridActivity.class);
                cardsGridActivityVars.putExtra("tagItemId", alMainTags.get(i).getTagId());
                startActivity(cardsGridActivityVars);

            }
        });

        Button btnTags = (Button) findViewById(R.id.btnTags);
        btnTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent TagsMainActivityVars = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(TagsMainActivityVars);

            }
        });

        Button btnCards = (Button) findViewById(R.id.btnCards);
        btnCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent CardsMainActivityVars = new Intent(getApplication(), CardsMainActivity.class);
                startActivity(CardsMainActivityVars);

            }
        });

    }

    // Control del menu del action bar
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater loadMenu = getMenuInflater();
        loadMenu.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.iCamera:
                //Toast.makeText(MainActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                Intent CardsMainActivityVars = new Intent(getApplication(), CardsMainActivity.class);
                startActivity(CardsMainActivityVars);
                return true;

            case R.id.iTags:
                //Toast.makeText(MainActivity.this, "Tags", Toast.LENGTH_SHORT).show();
                Intent TagsMainActivityVars = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(TagsMainActivityVars);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    */
    // fin Control del menu del action bar

    private void startDataBase () {

        // Creamos y abrimos la bbdd
        SQLiteDatabase db = Functions.accessToDb(MainActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase functions
        db.close();
    }

    @Override
    public void onBackPressed() { // anula el back button del despositivo

    }
}
