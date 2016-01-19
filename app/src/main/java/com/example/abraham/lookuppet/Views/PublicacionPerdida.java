package com.example.abraham.lookuppet.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.example.abraham.lookuppet.Controllers.HttpPost;
import com.example.abraham.lookuppet.R;
import com.example.abraham.lookuppet.Utilities.InteraccionCamara;
import com.example.abraham.lookuppet.Utilities.JsonHandler;
import com.example.abraham.lookuppet.Utilities.SystemUtilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PublicacionPerdida extends AppCompatActivity {

    String url="http://192.168.0.10:8080/proyecto/publicaciones";
    int presionado;
    ImageView imagen;
    ImageView imagen1;
    ImageView imagen2;
    ImageView imagen3;
    Spinner spTipoMascota;
    Spinner spComuna;
    EditText etCorreoContacto;
    EditText etTelefonoContacto;
    EditText etDescripcion;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int SELECTED_PICTURE = 2;
    String directorio = "";
    BroadcastReceiver br=null;
    String directorio1="";
    String directorio2="";
    String directorio3="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicacion_perdida);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = this.getIntent().getExtras();
        //Toast.makeText(this, b.getString("tipo"), Toast.LENGTH_LONG).show();
        imagen=null;
        imagen1 = (ImageView) findViewById(R.id.imagen1);
        imagen2 = (ImageView) findViewById(R.id.imagen2);
        imagen3 = (ImageView) findViewById(R.id.imagen3);
        spTipoMascota = (Spinner) findViewById(R.id.spinnerTipo);
        spComuna = (Spinner) findViewById(R.id.spinnerComuna);
        etCorreoContacto = (EditText) findViewById(R.id.etCorreoContacto);
        etTelefonoContacto = (EditText) findViewById(R.id.etTelefonoContacto);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        registerForContextMenu(imagen1);
        registerForContextMenu(imagen2);
        registerForContextMenu(imagen3);

        //para usar un hint en el spinner de comunas
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //agregar comunas
        adapter.add("La reina");
        adapter.add("San miguel");
        adapter.add("Comuna");

        spComuna.setAdapter(adapter);
        spComuna.setSelection(adapter.getCount()); //display hint

        //para usar un hint en el spinner de tipos
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //agregar comunas
        adapter1.add("Perro");
        adapter1.add("Gato");
        adapter1.add("Tipo de Mascota");

        spTipoMascota.setAdapter(adapter1);
        spTipoMascota.setSelection(adapter1.getCount()); //display hint
    }

    //PARA INFLAR EL MENU CUANDO SE APRETE LA IMAGEN
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto_imagenes, menu);
        if(v.equals(imagen1)){
         presionado=1;
        }
        else if(v.equals(imagen2)){
            presionado=2;
        }
        else{
            presionado=3;
        }
    }

    //PARA VER QUE ES LO QUE PASARA CUANDO SE APRETE CADA OPCION
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_subirDesdeGaleria) {
            //Toast.makeText(this,"selecciono subir desde galeria", Toast.LENGTH_LONG).show();
            //Se accede a la galeria del usuario con un intent
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECTED_PICTURE);
            return true;
        } else if (id == R.id.action_tomarFoto) {
            //Toast.makeText(this,"selecciono tomar foto", Toast.LENGTH_LONG).show();
                try{
                InteraccionCamara ic= new InteraccionCamara();
                Uri uri= ic.createImageFile();
                // Se continua solo si se pudo crear el archivo donde se guardara la foto
                if (uri!=null) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
                catch(Exception e){

                }

            return true;
        }
        return super.onContextItemSelected(item);
    }

    //SE SOBREESCRIBE EL METODO onActivityResult para ver que se hace con la imagen que se selecciono desde la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(presionado==1){
            imagen=imagen1;
        }
        else if(presionado==2){
            imagen=imagen2;
        }
        else if(presionado==3){
            imagen=imagen3;
        }
        Uri uri = data.getData();
        if(requestCode== SELECTED_PICTURE && resultCode == RESULT_OK) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filepath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap myBitmap = BitmapFactory.decodeFile(filepath);
            imagen.setImageBitmap(myBitmap);
            directorio = filepath;

            if(presionado==1){
                directorio1=directorio;
            }
            else if(presionado==2){
                directorio2=directorio;
            }
            else if(presionado==3){
                directorio3=directorio;
            }
        }

        else if(requestCode== REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Se guarda la foto en la galeria luego de que fue tomada
            InteraccionCamara ic = new InteraccionCamara();
            ic.galleryAddPic(uri);
            //ahora se muestra en el imageview al usuario;
            //Se usa directamente el contenido uri
            directorio=ic.mCurrentPhotoPath;
            Bitmap myBitmap = BitmapFactory.decodeFile(directorio);
            imagen.setImageBitmap(myBitmap);
            //Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_LONG).show();
        }

    }

    public void publicarOnClick(View v){
        String comuna= spComuna.getSelectedItem().toString();
        String tipo= spTipoMascota.getSelectedItem().toString();
        String telefono= etTelefonoContacto.getText().toString();
        String correo= etCorreoContacto.getText().toString();
        String descripcion= etDescripcion.getText().toString();
        //preparando la configuracion para subir la foto a Cloudinary
        //all se deben cambiar esos datos
        Map config = new HashMap();
        config.put("cloud_name", "dbdn1fern");
        config.put("api_key", "371799448131225");
        config.put("api_secret", "Qs1SsiR1MQGplcHvTCdudcki-yE");
        Cloudinary cloudinary = new Cloudinary(config);
        //Se debe transformar la imagen a un InputStream
        try {
            if(imagen!=null) {
                //Para la primera imagen
                File archivo1 = new File(directorio1);
                //Para la segunda imagen
                File archivo2 = new File(directorio2);
                //Para la tercera imagen
                File archivo3 = new File(directorio3);
                //Se suben las fotos
                cloudinary.uploader().upload(archivo1, Cloudinary.emptyMap());
                cloudinary.uploader().upload(archivo2, Cloudinary.emptyMap());
                cloudinary.uploader().upload(archivo3, Cloudinary.emptyMap());
            }
            else{
                Toast.makeText(PublicacionPerdida.this, "Debe subir al menos una imagen", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e){
           Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //se crea el jason handreler para construir el string en formato json
        JsonHandler jh= new JsonHandler();
        //se registra el br
        IntentFilter intentFilter = new IntentFilter("HttpPost");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //cuando se reciba una respuesta del post, se mostrará y se pasará al Home
                String rpta = intent.getStringExtra("rpta");
                Toast.makeText(PublicacionPerdida.this, rpta, Toast.LENGTH_LONG).show();
                finish();
            }
        };
        registerReceiver(br, intentFilter);
        SystemUtilities su= new SystemUtilities(getApplicationContext());
        if(!tipo.equals("") && !comuna.equals("") && !telefono.equals("") && !correo.equals("") && !descripcion.equals("")) {
            if (su.isNetworkAvailable()) {
                String objetoPost = jh.postPerdida(tipo, comuna, correo, telefono, descripcion);
                new HttpPost(getApplicationContext(), objetoPost).execute(url);
            } else {
                Toast.makeText(PublicacionPerdida.this, "Error. Revise su conexion a Internet", Toast.LENGTH_LONG).show();
            }
        }

        else{
            Toast.makeText(PublicacionPerdida.this, "Debe completar los campos", Toast.LENGTH_LONG).show();
        }
    }



}


