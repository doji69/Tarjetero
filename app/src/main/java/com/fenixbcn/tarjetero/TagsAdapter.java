package com.fenixbcn.tarjetero;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends ArrayAdapter {

    private Context mContext;
    private List<TagsClass> mListaTags = new ArrayList<>();

    public TagsAdapter(@NonNull Context context, @NonNull ArrayList<TagsClass> listaTags) {
        super(context, 0,listaTags);

        mContext = context;
        mListaTags = listaTags;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View vista = convertView;

        if(vista == null) {
            vista = LayoutInflater.from(mContext).inflate(R.layout.single_tag, parent, false);
        }

        TagsClass tagActual = mListaTags.get(position);

        TextView idTag = (TextView) vista.findViewById(R.id.tvTagId);
        idTag.setText(String.valueOf(tagActual.getTagId()));

        TextView nombreTag = (TextView) vista.findViewById(R.id.tvTagName);
        nombreTag.setText(tagActual.getNombreTag());

        TextView colorTag = (TextView) vista.findViewById(R.id.tvTagColor);
        colorTag.setBackgroundColor(tagActual.getTagColor());

        TextView tagsNum = (TextView) vista.findViewById(R.id.tvTagsNum);
        tagsNum.setText(String.valueOf(getNumCards(tagActual.getTagId())));

        return vista;

    }

    public int getNumCards (int mNumCards) {
        // funcion que devuekve el numero de cards asignados a un tag

        int numCards=0;
        SQLiteDatabase db = Functions.accessToDb(mContext); // la llamada a la apertura de la base de datos esta en una funcion en la clase Functions
        String[] select_params = new String[] {String.valueOf(mNumCards)};
        String selectNumCards = "SELECT Count(*) from cards where id_tag=?";
        Cursor cursor = db.rawQuery(selectNumCards, select_params);

        if (cursor.moveToFirst()) {

            do {

                numCards = cursor.getInt(0);

            } while (cursor.moveToNext());
        }

        db.close();

        return numCards;
    }
}
