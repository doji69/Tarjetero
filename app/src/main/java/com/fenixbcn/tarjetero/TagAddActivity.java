package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

public class TagAddActivity extends AppCompatActivity {

    TextView tvColorSelected;
    EditText etNombre;
    Button btnAddTag;
    int mDefaultColor, tagItemId;
    String tagAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        getSupportActionBar().hide(); // esconde el action bar en esta activity

        // comprobamos si se han pasado variables o no para la nueva actividad
        /*
        Intent AddTagActivityIntent = getIntent();

        if (AddTagActivityIntent.hasExtra("tagItemId")) {

            Bundle AddTagActivityVars = getIntent().getExtras();
            tagItemId = AddTagActivityVars.getInt("tagItemId", -1);
            tagAction = AddTagActivityVars.getString("tagAction", "sin accion");

            Toast.makeText(this, "hay parametros para " + tagAction + " " + tagItemId, Toast.LENGTH_SHORT).show();


        } else {

            Toast.makeText(this, "no hay parametros para " + tagAction + " " + tagItemId, Toast.LENGTH_SHORT).show();


        }
        */
        // fin comprobamos si se han pasado variables o no para la nueva actividad

        // variables y control para el selector de color

        tvColorSelected = (TextView) findViewById(R.id.tvColorSelected);
        mDefaultColor = ContextCompat.getColor(TagAddActivity.this, R.color.colorPrimary);
        Button btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        // fin variables y control para el selector de color

        // variables y control del insert en la base de datos

        etNombre = (EditText) findViewById(R.id.etNombre);
        btnAddTag = (Button) findViewById(R.id.btnSaveTag);

        etNombre.addTextChangedListener(new TextWatcher() { // control del campo vacio
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!etNombre.getText().toString().matches("")) {

                    btnAddTag.setEnabled(true);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (etNombre.getText().toString().matches("")) {

                    btnAddTag.setEnabled(false);

                }

            }
        });

        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = Functions.accessToDb(TagAddActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions

                String sqlInsert = "INSERT INTO tags (nombre_tag, color_tag) values ('"+ etNombre.getText()+"', "+ mDefaultColor+")";
                db.execSQL(sqlInsert);
                db.close();

                Toast.makeText(TagAddActivity.this, "Insert correcto",Toast.LENGTH_SHORT).show();

                Intent TagsMainActivityVars = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(TagsMainActivityVars);

            }
        });

        // fin variables y control del insert en la base de datos

        // control boton de cancelar

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent TagsMainActivityVars = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(TagsMainActivityVars);

            }
        });

        // fin control boton de cancelar
    }

    public void openColorPicker () {

        AmbilWarnaDialog colorPicked = new AmbilWarnaDialog(TagAddActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

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

    @Override
    public void onBackPressed() { // anula el back button del despositivo

    }

}
