package com.example.abraham.lookuppet.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.example.abraham.lookuppet.Controllers.HttpPost;
import com.example.abraham.lookuppet.Controllers.HttpPut;
import com.example.abraham.lookuppet.R;
import com.example.abraham.lookuppet.Utilities.JsonHandler;
import com.example.abraham.lookuppet.Utilities.SystemUtilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class EditarPerfil extends AppCompatActivity {

    ImageView fotoPerfil;
    EditText etNombreReal;
    EditText etNombreUser;
    EditText etDescripcion;
    BroadcastReceiver br=null;
    String url= "http://192.168.0.12:8080/proyecto/usuarios/editarusuario/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.abraham.lookuppet.R.layout.activity_editar_perfil);
        Toolbar toolbar = (Toolbar) findViewById(com.example.abraham.lookuppet.R.id.toolbar);
        setSupportActionBar(toolbar);
        //Se capturan las referencias de los objetos que hay en el layout
        // Para trabajar mas tarde con ellos
        fotoPerfil=(ImageView) findViewById(R.id.imageViewFotoPerfilEditar);
        etNombreReal=(EditText) findViewById(R.id.etNombreRealEditar);
        etNombreUser=(EditText) findViewById(R.id.etNombreUserEditar);
        etDescripcion=(EditText) findViewById(R.id.etDescripcionEditar);

    }

    public void editarOnClick(View v){
        //Se obtienen los datos de los objetos del layout
        final String nombreReal = etNombreReal.getText().toString();
        final String nombreUser= etNombreUser.getText().toString();
        final String descripcion= etDescripcion.getText().toString();
        SystemUtilities su= new SystemUtilities(getApplicationContext());
        String idUser= su.accederDatosPreferencias("idUser");
        JsonHandler jh= new JsonHandler();
        //se registra el br
        IntentFilter intentFilter = new IntentFilter("HttpPut");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String rpta = intent.getStringExtra("rpta");
                finish();
            }
        };
        registerReceiver(br, intentFilter);
        if(su.isNetworkAvailable()){
            String objetoPut = jh.putEditar(nombreReal,nombreUser,descripcion);
            new HttpPut(getApplicationContext(), objetoPut).execute(url+idUser);
            Intent i= getIntent();
            i.putExtra("nombreRealNuevo", nombreReal);
            i.putExtra("nombreUserNuevo", nombreUser);
            i.putExtra("descripcionNueva", descripcion);
            setResult(RESULT_OK, i);
        } else {
            Toast.makeText(EditarPerfil.this, "Error. Revise su conexion a Internet", Toast.LENGTH_LONG).show();
        }
    }

}
