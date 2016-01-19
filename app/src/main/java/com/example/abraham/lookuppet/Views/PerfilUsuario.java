package com.example.abraham.lookuppet.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abraham.lookuppet.Controllers.HttpGet;
import com.example.abraham.lookuppet.R;
import com.example.abraham.lookuppet.Utilities.JsonHandler;
import com.example.abraham.lookuppet.Utilities.SystemUtilities;

public class PerfilUsuario extends AppCompatActivity {

    ImageView fotoPerfil;
    ImageButton botonMisAdopciones;
    ImageButton botonMisPerdidas;
    ImageButton botonMisSeguimientos;
    ImageButton botonMisMascotas;
    TextView tvDescripcion;
    ListView listaDatos;
    String url="http://192.168.0.12:8080/usuarios/";
    BroadcastReceiver br=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //se cargan los datos
        IntentFilter intentFilter = new IntentFilter("httpData");
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                JsonHandler jh = new JsonHandler();
                String[] datos= jh.getPerfilUser(intent.getStringExtra("data"));
                tvDescripcion.setText("Mi nombre es: "+ datos[0]+ "  " +
                "Mi nombre de usuario es: "+ datos[1] + " " + " Mi descripcion es: " + datos[2]);
            }
        };
        registerReceiver(br, intentFilter);
        SystemUtilities su = new SystemUtilities(getApplicationContext());
        String idUser= su.accederDatosPreferencias("idUser");
        if (su.isNetworkAvailable()) {
            new HttpGet(getApplicationContext()).execute(url+idUser);
        }
        else{
            Toast.makeText(this, "Error. Revise su conexion a Internet", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.editarPerfil){
            Intent i= new Intent(this, EditarPerfil.class);
            startActivityForResult(i,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
                Bundle b= data.getExtras();
                String nuevoNombreReal= b.getString("nombreRealNuevo");
                String nuevoNombreUser= b.getString("nombreUserNuevo");
                String nuevaDescripcion= b.getString("descripcionNueva");
            tvDescripcion.setText("Mi nombre es: "+ nuevoNombreReal+ "  " +
                    "Mi nombre de usuario es: "+ nuevoNombreUser + " " + " Mi descripcion es: " + nuevaDescripcion);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
