package com.fenixbcn.tarjetero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class CardModifyActivity extends AppCompatActivity {

    String cardSelected;
    int tagItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_modify);

        Bundle cardsGridActivityVars = getIntent().getExtras();
        cardSelected = cardsGridActivityVars.getString("card name", "no image");
        tagItemId = cardsGridActivityVars.getInt("tagItemId", -1);

        Toast.makeText(CardModifyActivity.this, "Show card " + cardSelected, Toast.LENGTH_SHORT).show();
    }
}
