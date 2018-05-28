package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.Cursor;
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

public class TagModifyActivity extends AppCompatActivity {

    TextView tvColorSelected;
    EditText etNombre;
    Button btnModifyTag;
    int mDefaultColor, tagItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_tag);

        getSupportActionBar().hide(); // esconde el action bar en esta activity

        // variables y control para el selector de color

        tvColorSelected = (TextView) findViewById(R.id.tvColorSelected);
        mDefaultColor = ContextCompat.getColor(TagModifyActivity.this, R.color.colorPrimary);
        Button btnColorPicker = (Button) findViewById(R.id.btnColorPicker);
        btnColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        // fin variables y control para el selector de color

        // control para le recuperacion de los datos del tag a modificar a partir del id recibido
        // de parametro en el Intent

        etNombre = (EditText) findViewById(R.id.etNombre);
        Bundle tagAddActivityVars = getIntent().getExtras();
        tagItemId = tagAddActivityVars.getInt("tagItemId", -1);

        String stTagsItemId = String.valueOf(tagItemId);

        SQLiteDatabase db = Functions.accessToDb(TagModifyActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
        String[] select_params = new String[] {stTagsItemId};
        String sqlSelect = "SELECT * FROM tags WHERE id_tag=?";
        Cursor cursor = db.rawQuery(sqlSelect, select_params);

        if (cursor.moveToFirst()) {

            do {

                etNombre.setText(cursor.getString(1));
                tvColorSelected.setBackgroundColor(cursor.getInt(2));

            } while (cursor.moveToNext());
        }

        db.close();

        // fin control para le recuperacion de los datos de tag a modificar a partir del id recibido
        // de parametro en el Intent

        // variables y control del update en la base de datos

        btnModifyTag = (Button) findViewById(R.id.btnSaveTag);

        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!etNombre.getText().toString().matches("")) {

                    btnModifyTag.setEnabled(true);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (etNombre.getText().toString().matches("")) {

                    btnModifyTag.setEnabled(false);

                }

            }
        });

        btnModifyTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = Functions.accessToDb(TagModifyActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
                String sqlUpdate = "UPDATE tags SET nombre_tag='" + etNombre.getText() + "' , color_tag="+ mDefaultColor + " WHERE id_tag=" + tagItemId;
                db.execSQL(sqlUpdate);
                db.close();

                Toast.makeText(TagModifyActivity.this, "Update correcto " + tagItemId,Toast.LENGTH_SHORT).show();

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

                Intent tagsMainActivityVars = new Intent(getApplication(), TagsMainActivity.class);
                startActivity(tagsMainActivityVars);

            }
        });

        // fin control boton de cancelar

    }

    public void openColorPicker () {

        AmbilWarnaDialog colorPicked = new AmbilWarnaDialog(TagModifyActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

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
