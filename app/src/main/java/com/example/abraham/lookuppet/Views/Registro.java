package com.example.abraham.lookuppet.Views;

import android.app.FragmentTransaction;
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
import com.example.abraham.lookuppet.Utilities.BaseNaviD;
import com.example.abraham.lookuppet.Utilities.JsonHandler;
import com.example.abraham.lookuppet.Utilities.SystemUtilities;

public class Registro extends AppCompatActivity {
    EditText etNombreReal;
    EditText etNombreUser;
    EditText etCorreo;
    EditText etContra;
    BroadcastReceiver br=null;
    String url= "http://192.168.0.12:8080/proyecto/usuarios/crearusuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        etNombreReal= (EditText) findViewById(R.id.etNombreReal);
        etNombreUser= (EditText) findViewById(R.id.etNombreUsuario);
        etCorreo= (EditText) findViewById(R.id.etCorreo);
        etContra= (EditText) findViewById(R.id.etContra);
    }


    public void registrarmeOnClick(View v){
        //se toman los ddatos desde los edit text
        String nombre= etNombreReal.getText().toString();
        final String username=etNombreUser.getText().toString();
        String correo=etCorreo.getText().toString();
        final String contra=etContra.getText().toString();
        final SystemUtilities su= new SystemUtilities(getApplicationContext());
        //se crea el jason handreler para construir el string en formato json
        JsonHandler jh= new JsonHandler();
        //se registra el br
        IntentFilter intentFilter = new IntentFilter("HttpPost");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //cuando se reciba una respuesta del post, se guardan los datos y se lleva al Home
                    //se guardan el usuairo y la contra en las preferencias
                    su.guardarEnPreferencias(username, contra, "id");
                    Toast.makeText(Registro.this, "Registrado con exito!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Registro.this, Home.class);
                    startActivity(i);
                    finish();
            }
        };
        registerReceiver(br, intentFilter);
        //Se verifica que hayan datos en los campos
        if(!nombre.equals("") && !username.equals("") && !correo.equals("") && !contra.equals("")) {
            if (su.isNetworkAvailable()) {
                String objetoPost = jh.postUser(nombre, username, correo, contra);
                new HttpPost(getApplicationContext(), objetoPost).execute(url);
            } else {
                Toast.makeText(Registro.this, "Error. Revise su conexion a Internet", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(Registro.this, "Debe rellenar los campos", Toast.LENGTH_LONG).show();
        }
    }
}
