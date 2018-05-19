package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddTagActivity extends AppCompatActivity {

    TextView tvColorSelected;
    int mDefaultColor, tagItemId;
    String tagAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        // comprobamos si se han pasado variables o no para la nueva actividad

        Intent AddTagActivityIntent = getIntent();

        if (AddTagActivityIntent.hasExtra("tagItemId")) {

            Bundle AddTagActivityVars = getIntent().getExtras();
            tagItemId = AddTagActivityVars.getInt("tagItemId", -1);
            tagAction = AddTagActivityVars.getString("tagAction", "sin accion");

            Toast.makeText(this, "hay parametros para " + tagAction + " " + tagItemId, Toast.LENGTH_SHORT).show();


        } else {

            Toast.makeText(this, "no hay parametros para " + tagAction + " " + tagItemId, Toast.LENGTH_SHORT).show();


        }

        // fin comprobamos si se han pasado variables o no para la nueva actividad

        // variables y control para el selector de color

        tvColorSelected = (TextView) findViewById(R.id.tvColorSelected);
        mDefaultColor = ContextCompat.getColor(AddTagActivity.this, R.color.colorPrimary);
        Button btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        // fin variables y control para el selector de color

        // variables y control del insert en la base de datos

        final EditText etNombre = (EditText) findViewById(R.id.etNombre);
        Button btnAddTag = (Button) findViewById(R.id.btnSaveTag);

        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DataBaseClass dbTajetero = new DataBaseClass(AddTagActivity.this, "tarjetero", null, 1);
                SQLiteDatabase db = dbTajetero.getWritableDatabase();
                String sqlInsert = "INSERT INTO tags (nombre_tag, color_tag) values ('"+ etNombre.getText()+"', "+ mDefaultColor+")";
                //System.out.println(sqlInsert);
                db.execSQL(sqlInsert);
                db.close();

                Toast.makeText(AddTagActivity.this, "Insert correcto",Toast.LENGTH_SHORT).show();

                Intent viewTagsViewActivity = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(viewTagsViewActivity);

            }
        });

        // fin variables y control del insert en la base de datos
    }

    public void openColorPicker () {

        AmbilWarnaDialog colorPicked = new AmbilWarnaDialog(AddTagActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                mDefaultColor = color;
                tvColorSelected.setBackgroundColor(mDefaultColor);

            }
        });
        colorPicked.show();
    }
}
