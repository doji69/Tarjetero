package com.fenixbcn.tarjetero;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CardsMainActivity extends AppCompatActivity {

    private static final String TAG = "directory";
    public static final int externaStoragePermision = 100; // codigo unico de referencia para validar el permiso
    private static final int camPermision = 10; // codigo unico de referencia de respuesta de la activity
    private ImageView takedPhoto;
    private String imageFullPath = "";
    private String galleryDirectory = "image gallery";
    private File fGalleryDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_main);

        createImageDirectory();

        takedPhoto = (ImageView) findViewById(R.id.iwTakedPhoto);

        takedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "llamanda a la camara", Toast.LENGTH_LONG).show();

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    callingCamApp();

                } else {

                    String[] permisos_respondidos = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permisos_respondidos, externaStoragePermision);

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == externaStoragePermision) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                callingCamApp();

            } else {

                Toast.makeText(this, "No se puede guardar datos sin permisos", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void callingCamApp () {

        Intent callCameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e){
            e.printStackTrace();
        }

        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        Uri imageUri = FileProvider.getUriForFile(this, authorities, photoFile);

        //callCameraApp.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

        callCameraApp.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(callCameraApp, camPermision);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == camPermision && resultCode == RESULT_OK) {
            //Toast.makeText(this, "foto tomada correctamente", Toast.LENGTH_SHORT).show();
            //Bitmap capturedPhoto = (Bitmap) data.getExtras().get("data");
            //takedPhoto.setImageBitmap(capturedPhoto);

            //Bitmap capturedPhoto = BitmapFactory.decodeFile(imageFullPath);
            //takedPhoto.setImageBitmap(capturedPhoto);

            rotateImage(setReducedImageSize());
            deleteCamFile();
        }
    }

    private void createImageDirectory () {

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        fGalleryDirectory = new File(storageDir, galleryDirectory);

        if (!fGalleryDirectory.exists()) {
            fGalleryDirectory.mkdirs();
        }

    }

    File createImageFile () throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);


        //Log.d(TAG, "el directorio es " + storageDir);

        File image = File.createTempFile(imageFileName, ".jpg", fGalleryDirectory);

        //Log.d(TAG, "el archivo es " + image);

        imageFullPath = image.getAbsolutePath();

        //Log.d(TAG, "el full path es " + imageFullPath);

        return image;

    }

    private void deleteCamFile () {

        String[] fileList = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE,

        };

        final Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                fileList, null, null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();

            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String image_path = cursor.getString(column_index_data);

            File file = new File(image_path);

            if (file.exists()) {
                file.delete();

            }

        }

    }

    private Bitmap setReducedImageSize () {

        int targetImageViewWidth = takedPhoto.getWidth();
        int targetImageViewHeight = takedPhoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFullPath, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = 0;
        scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = 2;
        bmOptions.inJustDecodeBounds = false;
        //Bitmap photoReducedSize = BitmapFactory.decodeFile(imageFullPath, bmOptions);
        //takedPhoto.setImageBitmap(photoReducedSize);

        return BitmapFactory.decodeFile(imageFullPath, bmOptions);

    }

    private void rotateImage (Bitmap bitmap) {

        ExifInterface exifInterface = null;

        try {

            exifInterface = new ExifInterface(imageFullPath);
        } catch (IOException e) {

            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix,true);
        takedPhoto.setImageBitmap(rotatedBitmap);

    }
}
