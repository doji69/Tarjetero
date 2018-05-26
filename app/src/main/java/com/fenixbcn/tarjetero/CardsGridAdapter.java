package com.fenixbcn.tarjetero;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardsGridAdapter  extends ArrayAdapter {

    private Context mContext;
    private List<File> mListaCardsFiles = new ArrayList<>();

    public CardsGridAdapter(@NonNull Context context, @NonNull ArrayList<File> listaCards) {
        super(context, 0, listaCards);

        mContext = context;
        mListaCardsFiles = listaCards;
    }

    @Override
    public int getCount() {
        return mListaCardsFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mListaCardsFiles.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vista =  convertView;

        if (vista == null) {
            vista = LayoutInflater.from(mContext).inflate(R.layout.single_card, parent,false);
        }

        ImageView ivSingleImage = (ImageView) vista.findViewById(R.id.ivSingleCard);
        ivSingleImage.setImageURI(Uri.parse(getItem(position).toString()));

        return vista;
    }
}
