package com.example.abraham.lookuppet.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abraham.lookuppet.Controllers.HttpPost;
import com.example.abraham.lookuppet.R;
import com.example.abraham.lookuppet.Utilities.JsonHandler;
import com.example.abraham.lookuppet.Utilities.SystemUtilities;

public class InicioSesion extends AppCompatActivity {

    String url="http://192.1680.12:8080/proyecto/usuarios/logearusuario/";
    BroadcastReceiver br=null;
    EditText etNombreUser;
    EditText etContra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etContra= (EditText) findViewById(R.id.etContra);
        etNombreUser=(EditText) findViewById(R.id.etNombreUser);
    }


    public void iniciarOnClick(View v){
        //Hay que hacer un POST a la direccion
        //se crea el jason handreler para construir el string en formato json
        final JsonHandler jh= new JsonHandler();
        //Se obtiene los datos del usuario
        final String nombreUser= etNombreUser.getText().toString();
        final String contra= etContra.getText().toString();
        final SystemUtilities su= new SystemUtilities(getApplicationContext());
        if(nombreUser.equals("") && contra.equals("")){
            Toast.makeText(this, "Debe completar los campos", Toast.LENGTH_LONG).show();
            return;
        }
        //se registra el br
        IntentFilter intentFilter = new IntentFilter("HttpPost");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //cuando se reciba una respuesta del post,
                // Si contiene idUsuario, significa que si existe, se guardan los datos y se pasa al HOME
                //De lo contrario se muestra un mensaje de error
                String rpta = intent.getStringExtra("rpta");
                    String idUser = jh.getUser(rpta);
                    su.guardarEnPreferencias(nombreUser,contra, idUser);
                    Intent i = new Intent(InicioSesion.this, Home.class);
                    startActivity(i);
                    finish();
            }
        };
        registerReceiver(br, intentFilter);
        if(su.isNetworkAvailable()){
            String objetoPost = jh.postLogeo(nombreUser, contra);
            new HttpPost(getApplicationContext(), objetoPost).execute(url);
        } else {
            Toast.makeText(InicioSesion.this, "Error. Revise su conexion a Internet", Toast.LENGTH_LONG).show();
        }
    }

}
