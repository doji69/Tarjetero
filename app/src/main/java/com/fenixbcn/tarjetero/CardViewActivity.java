package com.fenixbcn.tarjetero;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class CardViewActivity extends AppCompatActivity {

    String cardSelectd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        Bundle cardsGridActivityVars = getIntent().getExtras();
        cardSelectd = cardsGridActivityVars.getString("card name", "no image");

        Toast.makeText(CardViewActivity.this, "Show card " + cardSelectd, Toast.LENGTH_SHORT).show();

        ImageView ivViewSingleCard = (ImageView) findViewById(R.id.ivViewSingleCard);
        ivViewSingleCard.setImageURI(Uri.parse(cardSelectd));

    }
}
