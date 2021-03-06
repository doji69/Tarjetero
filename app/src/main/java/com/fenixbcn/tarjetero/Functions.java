package com.fenixbcn.tarjetero;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Functions {

    public static SQLiteDatabase accessToDb (Context contex) {

        DataBaseClass dbTajetero = new DataBaseClass(contex, "tarjetero", null, 2);
        SQLiteDatabase db = dbTajetero.getWritableDatabase();

        return db;
    }

    public static void deleteCamFile (ContentResolver contentResolver, Context context) {

        String[] fileList = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE,

        };

        final Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                fileList, null, null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();

            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String image_path = cursor.getString(column_index_data);

            File file = new File(image_path);

            if (file.exists()) {
                file.delete();

                // re-escanea la galeria de fotos y borra el place holder de la fotos que no existen
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        //Log.e("ExternalStorage", "Scanned " + path + ":");
                        //Log.e("ExternalStorage", "-> uri=" + uri);
                    }
                });

                // fin re-escanea la galeria de fotos y borra el place holder de la fotos que no existen
            }
        }
    }
}
