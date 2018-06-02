package com.fenixbcn.tarjetero;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class CardViewActivity extends AppCompatActivity {

    String cardSelected;
    int tagItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        // recuperamos variables de Intent

        Bundle cardsGridActivityVars = getIntent().getExtras();
        cardSelected = cardsGridActivityVars.getString("card name", "no image");
        tagItemId = cardsGridActivityVars.getInt("tagItemId", -1);

        //Toast.makeText(CardViewActivity.this, "Show card " + cardSelectd, Toast.LENGTH_SHORT).show();

        // asignamos la imagen al imageView

        ImageView ivViewSingleCard = (ImageView) findViewById(R.id.ivViewSingleCard);
        ivViewSingleCard.setImageURI(Uri.parse(cardSelected));

        ImageButton btnCancelCard = (ImageButton) findViewById(R.id.btnCancelCard);
        btnCancelCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cardsGridActivityVars = new Intent(getApplication(), CardsGridActivity.class);
                cardsGridActivityVars.putExtra("tagItemId", tagItemId );
                startActivity(cardsGridActivityVars);
            }
        });



    }
}
