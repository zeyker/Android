package com.example.abraham.lookuppet.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abraham.lookuppet.R;
import com.example.abraham.lookuppet.Views.Home;
import com.example.abraham.lookuppet.Views.PerfilUsuario;
import com.example.abraham.lookuppet.Views.PublicacionPerdida;

import java.util.ArrayList;

//Clase que sirve de base para el navigation drawer
public class BaseNaviD extends AppCompatActivity {
    protected FrameLayout actContent;
    private String[] opciones;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private CharSequence tituloSec;
    private CharSequence tituloApp;
    private ActionBarDrawerToggle drawerToggle;

    //Se sobreescribe el metodo setContentView para que cuando las clases hijas lo implementen,
    //se les inflen tambien los layouts del navigation drawer
    //de esta forma, para que aparesca el navigation drawer en una actividad, esta solo debe
    //extender de la clase base.
    @Override
    public void setContentView(final int layoutResID) {
        // Your base layout here
        drawerLayout= (DrawerLayout) getLayoutInflater().inflate(R.layout.content_base_navi_d, null);
        actContent= (FrameLayout) drawerLayout.findViewById(R.id.contenedorFrame);

        // Setting the content of layout your provided to the act_content frame
        getLayoutInflater().inflate(layoutResID, actContent, true);
        super.setContentView(drawerLayout);

        navigationDrawer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navi_d);
        navigationDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base_navi_d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.publicar) {
            return true;
        }

        String tipo="";
        Intent i= new Intent(this, PublicacionPerdida.class);
        //Para evitar mal funcionamiento con la navigation drawer
        //Si la opcion seleccionada es el drawer toogle, solo se vuelve
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement

        if (id == R.id.publicarPerdida) {
            tipo="perdida";
        }

        if (id == R.id.publicarAdopcion) {
            tipo="adopcion";
        }
        i.putExtra("tipo", tipo);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    public void navigationDrawer(){
        SharedPreferences preferencias = getSharedPreferences("Mis preferencias", Context.MODE_PRIVATE);
        String id = preferencias.getString("id", "");
        // Toast.makeText(this, "Mi id es "+ id, Toast.LENGTH_LONG).show();
        //Parte del navigation drawer
        opciones= new String[] {"Inicio","Mi perfil", "Ajustes", "Solicitudes pendientes", "Buscar amigos",  "Buscar noticia", "Cerrar sesion"};
        drawerLayout=(DrawerLayout) findViewById(R.id.contenedorPrincipal);
        listView=(ListView) findViewById(R.id.menuIzq);
        //Nueva lista de drawer items
        ArrayList<DrawerItem> items = new ArrayList<DrawerItem>();
        items.add(new DrawerItem(opciones[0],R.drawable.ic_action_action_home));
        items.add(new DrawerItem(opciones[1],R.drawable.ic_action_social_people));
        listView.setAdapter(new DrawerListAdapter(this, items));
        //Con esto, cuando pinchen una opcion se cambie el frame
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = null;
                if (position == 0) {
                    Intent i = new Intent(BaseNaviD.this, Home.class);
                    startActivity(i);
                }
                if (position == 1) {
                    Intent i= new Intent(BaseNaviD.this, PerfilUsuario.class);
                    startActivity(i);
                }
                if (position == 2) {
                    //fragment = new FragmentoAjustes();
                }
                if (position == 3) {
                    //fragment = new FragmentoAmistadesPendientes();
                }
                if (position == 4) {
                    //fragment = new FragmentoBuscarAmigos();
                }
                if (position == 5) {
                    //fragment = new FragmentoBuscarNoticia();
                }
                if (position == 6) {
                    //fragment = new FragmentoCerrarSesion();
                }
                if (fragment != null) {
                    //Para cargar los fragmentos en el espacio donde pusimos el contenedor de fragmentos
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    //Se le indica que comenzara una transaccion para colocar dentro del contenedor de frame,
                    // el ffragmento que yo quiero, se confirma con commit
                    fragmentManager.beginTransaction().replace(R.id.contenedorFrame, fragment).commit();
                    //Se marca cual fue el fragmento seleccionado
                    listView.setItemChecked(position, true);
                    //Se muestra en pantalla,arriba en el titulo, cual es el elemento(fragmento) seleccionado
                    tituloSec = opciones[position];
                    //Se setea el titulo de la seccion en tal, utilizando el SupportActionBar
                    //
                    //SharedPreferences preferencias = getSharedPreferences("Mis preferencias", Context.MODE_PRIVATE);
                    //getSupportActionBar().setTitle(preferencias.getString("nombre_user", ""));
                    drawerLayout.closeDrawer(listView);
                }
            }
        });

        tituloSec=getTitle();
        tituloApp=getTitle();
        //Se utiliza drawerToggle para poner el icono del navigation drawer
        drawerToggle= new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_chrome_reader_mode_black_24dp,R.string.abierto,R.string.cerrado)
                //Se utilizan estos metodos para sacar elementos del action bar que no tengan nada que ver con el menu de los fragmentos
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ActivityCompat.invalidateOptionsMenu(BaseNaviD.this);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ActivityCompat.invalidateOptionsMenu(BaseNaviD.this);
            }
        };
        //Para que cuando se pincha un valor, se tiene un estado, y se pincha otro valor, este tiene otro estado
        drawerLayout.setDrawerListener(drawerToggle);
        //Algunas configuraciones
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onBackPressed(){
        //ES PARA VER SI ESTA ABIERTO EL NAVIGATION DRAWER,
        //SI ESTA ABIERTO, SE CIERRA
        //SI NO, SE CIERRAN TODAS LAS ACTIVIDADES ANTERIORES
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else {
            super.finish();
            finish();
        }
    }

    //PARTE DEL NAVIGATION DRAWER


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //con esto se hace que si aparece el menu, se eliminan todos los botones de la actiobar
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}

