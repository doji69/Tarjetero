package com.fenixbcn.tarjetero;

public class TagsSpinnerClass {
    int id_tag, color_tag;
    String name_tag;

    public TagsSpinnerClass (int id_tag, String name_tag, int color_tag) {

        this.id_tag = id_tag;
        this.name_tag = name_tag;
        this.color_tag = color_tag;

    }

    public int getId_tag() {
        return id_tag;
    }

    public String getName_tag() {
        return name_tag;
    }

    public int getColor_tag() {
        return color_tag;
    }
}
