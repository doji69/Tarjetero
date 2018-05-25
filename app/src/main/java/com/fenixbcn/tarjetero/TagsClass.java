package com.fenixbcn.tarjetero;

import android.database.sqlite.SQLiteDatabase;

public class TagsClass {

    int tagId, tagColor;
    String tagName;

    public TagsClass(int id_tag, String nombre_tag, int color_tag) {
        this.tagId = id_tag;
        this.tagName = nombre_tag;
        this.tagColor = color_tag;
    }

    public int getTagId() {

        return tagId;
    }

    public String getNombreTag () {

        return tagName;
    }

    public int getTagColor() {

        return tagColor;
    }

}
