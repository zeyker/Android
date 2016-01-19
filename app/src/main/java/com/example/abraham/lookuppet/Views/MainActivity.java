package com.example.abraham.lookuppet.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.abraham.lookuppet.R;

public class MainActivity extends AppCompatActivity {

    Button btnIniciarSes;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //se verifica si ya hay un registor de que el usuario accedio a la aplicacion, de ser asi se le lleva directamente al Home
        SharedPreferences preferencias = getSharedPreferences("Mis preferencias", MODE_PRIVATE);
        if (!preferencias.getString("username", "").equals("")) {
            Intent i = new Intent(this, Home.class);
            startActivity(i);
            return;
        } else {
            setContentView(R.layout.activity_main);
            //Codigo para que no hayan 2 barras, se setea la que se añade al layout como la barra de herramientas de soporte
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //Se capturan los objetos como botones para poder trabajarlos apenas se cree la vista
            btnIniciarSes = (Button) findViewById(R.id.buttonIniciarSesion);
            btnRegistrar = (Button) findViewById(R.id.buttonRegistrar);

            //Codigo para hacer funcionar un botón flotante
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void iniciarSesionOnClick(View v){
        Intent i= new Intent(this, InicioSesion.class);
        startActivity(i);
    }

    public void registrarOnClick(View v){
        Intent i= new Intent(this, Registro.class);
        startActivity(i);
    }
}
