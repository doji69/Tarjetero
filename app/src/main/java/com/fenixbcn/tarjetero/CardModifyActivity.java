package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CardModifyActivity extends AppCompatActivity {

    private static final String TAG = "tarjetero";
    String cardSelected;
    int tagItemId,cardItemIdSel;

    Spinner sTags;
    final List<TagsSpinnerClass> lTagsSpinner = new ArrayList<>();
    int id_tag;

    private Button btnSaveCard;
    private ImageButton btnCancelCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_modify);

        Bundle cardsGridActivityVars = getIntent().getExtras();
        cardSelected = cardsGridActivityVars.getString("card name", "no image");
        tagItemId = cardsGridActivityVars.getInt("tagItemId", -1);
        cardItemIdSel = cardsGridActivityVars.getInt("cardItemIdSel");

        //Toast.makeText(CardModifyActivity.this, "Show card " + cardSelected, Toast.LENGTH_SHORT).show();

        ImageView iwTakedPhoto = (ImageView) findViewById(R.id.iwTakedPhoto);
        iwTakedPhoto.setImageURI(Uri.parse(cardSelected));

        // rellenado del spinner con los tags de la base de datos

        SQLiteDatabase db = Functions.accessToDb(this);
        sTags = (Spinner) findViewById(R.id.sTags);

        String sqlSelect = "SELECT * FROM tags";
        Cursor cursor = db.rawQuery(sqlSelect, null);

        if (cursor.moveToFirst()) {

            do {

                lTagsSpinner.add(new TagsSpinnerClass(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));

            } while (cursor.moveToNext());
        }

        db.close();

        TagsSpinnerAdapter tagsSpinnerAdapter = new TagsSpinnerAdapter(this, R.layout.spinner_single_tag, lTagsSpinner);
        sTags.setAdapter(tagsSpinnerAdapter);
        //Log.d(TAG, "el cardItemIdSel "+ cardItemIdSel );
        sTags.setSelection(cardItemIdSel);

        sTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                id_tag = lTagsSpinner.get(i).getId_tag();

                //Toast.makeText(CardsMainActivity.this, id_tag + " " + imageFullPath, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // fin rellenado del spinner con los tags de la base de datos

        // control boton de cancel

        btnCancelCard = (ImageButton) findViewById(R.id.btnCancelCard);
        btnCancelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cardsGridActivityVars = new Intent(getApplication(), CardsGridActivity.class);
                cardsGridActivityVars.putExtra("tagItemId", tagItemId);
                startActivity(cardsGridActivityVars);

            }
        });
        // fin control boton de cancel

        // control boton de guardado de la foto con el tag

        btnSaveCard = (Button) findViewById(R.id.btnSaveCard);

        btnSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db = Functions.accessToDb(CardModifyActivity.this); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
                String sqlUpdate = "UPDATE cards SET id_tag = " + id_tag + " WHERE nombre_card = '" + cardSelected + "'";
                db.execSQL(sqlUpdate);
                db.close();

                //Log.d(TAG, "la sentencia update es " + sqlUpdate);
                Toast.makeText(CardModifyActivity.this, "Update correcto" ,Toast.LENGTH_LONG).show();

                Intent MainActivityVars = new Intent(getApplication(), MainActivity.class);
                startActivity(MainActivityVars);

            }
        });

        // fin control boton de guardado de la foto con el tag


    }
}
