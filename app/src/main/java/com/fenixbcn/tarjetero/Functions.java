package com.fenixbcn.tarjetero;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Functions {

    public static SQLiteDatabase accessToDb (Context contex) {
        
        DataBaseClass dbTajetero = new DataBaseClass(contex, "tarjetero", null, 1);
        SQLiteDatabase db = dbTajetero.getWritableDatabase();

        return db;
    }
}
