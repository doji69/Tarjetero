package com.fenixbcn.tarjetero;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TagsSpinnerAdapter extends ArrayAdapter {

    private Context context;
    private List<TagsSpinnerClass> lSpinnerTags;

    public TagsSpinnerAdapter(@NonNull Context context, int resource, List<TagsSpinnerClass> lSpinnerTags) {
        super(context, resource, lSpinnerTags);

        this.context = context;
        this.lSpinnerTags = lSpinnerTags;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return spinnerView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return spinnerView(position, convertView, parent);
    }

    private View spinnerView (int position, @Nullable View tagView, @Nullable ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customTagView = layoutInflater.inflate(R.layout.spinner_single_tag, parent, false);

        TextView tvTagId = (TextView) customTagView.findViewById(R.id.tvTagId);
        tvTagId.setText(String.valueOf(lSpinnerTags.get(position).getId_tag()));

        TextView tvTagName = (TextView) customTagView.findViewById(R.id.tvTagName);
        tvTagName.setText(lSpinnerTags.get(position).getName_tag());

        TextView tvTagColor = (TextView) customTagView.findViewById(R.id.tvTagColor);
        tvTagColor.setBackgroundColor(lSpinnerTags.get(position).getColor_tag());

        return customTagView;
    }

}
