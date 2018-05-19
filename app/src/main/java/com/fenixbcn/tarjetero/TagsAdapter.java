package com.fenixbcn.tarjetero;

import android.content.Context;
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

        TextView nombreTag = (TextView) vista.findViewById(R.id.tvTagName);
        nombreTag.setText(tagActual.getNombreTag());

        TextView colorTag = (TextView) vista.findViewById(R.id.tvTagColor);
        colorTag.setBackgroundColor(tagActual.getTagColor());

        return vista;

    }


}
