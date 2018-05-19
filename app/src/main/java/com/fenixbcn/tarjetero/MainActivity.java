package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startDataBase(); // arrancamos la base de datos

    }

    // Control del menu del action bar
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
                Toast.makeText(MainActivity.this, "Camera", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.iTags:
                //Toast.makeText(MainActivity.this, "Tags", Toast.LENGTH_SHORT).show();
                Intent viewTagsViewActivity = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(viewTagsViewActivity);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // fin Control del menu del action bar

    private void startDataBase () {

        // Creamos y abrimos la bbdd
        DataBaseClass dbTajetero = new DataBaseClass(this, "tarjetero", null, 1);
        SQLiteDatabase db = dbTajetero.getWritableDatabase();
        dbTajetero.close();

    }
}
