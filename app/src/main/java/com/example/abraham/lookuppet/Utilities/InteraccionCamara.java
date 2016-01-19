package com.example.abraham.lookuppet.Utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abraham on 27-12-2015.
 */
public class InteraccionCamara extends AppCompatActivity{

    public String mCurrentPhotoPath;
    private final String ruta_fotos = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

    //Codigo para crear el archivo en donde se guardara la foto que se saca con la camara
    public Uri createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String file = ruta_fotos+ "pic_" + timeStamp + ".jpg";
        File mi_foto= new File(file);
        try{
            mi_foto.createNewFile();
        }
        catch(Exception e){
            Log.e("ERROR ", "Error:" + e);
        }
        Uri uri = Uri.fromFile( mi_foto );
        return uri;
    }

    //Para agregar la imagen a la galeria
    //SE debe pasar el contenido uri de la foto que se saco con la camara
    //el contenido uri se saca del intent en onActivityresult
    public void galleryAddPic(Uri contentUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        //Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
