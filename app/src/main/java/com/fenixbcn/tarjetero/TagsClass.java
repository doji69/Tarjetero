package com.fenixbcn.tarjetero;

public class TagsClass {

    int tagColor;
    String tagName;

    public TagsClass(String nombre_tag, int color_tag) {
        this.tagName = nombre_tag;
        this.tagColor = color_tag;
    }

    public String getNombreTag () {

        return tagName;
    }

    public int getTagColor() {

        return tagColor;
    }
}
