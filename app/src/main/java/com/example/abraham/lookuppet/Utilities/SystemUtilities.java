package com.example.abraham.lookuppet.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by abraham on 27-12-2015.
 */
public class SystemUtilities {

    Context context;

    /**
     * Constructor
     */
    public SystemUtilities(Context context) {
        this.context = context;
    }// SystemUtilities(Context context)

    /**
     * Método que consulta el estado de la conexión a Internet
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }// isNetworkAvailable()

    public void guardarEnPreferencias(String nombreUser, String contra, String idUser){
        SharedPreferences preferencias= this.context.getSharedPreferences("Mis preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("nombreUser", nombreUser);
        editor.putString("contrasena",contra);
        editor.putString("idUser", idUser);
        editor.commit();
    }

    public String accederDatosPreferencias(String nombreDato){
        SharedPreferences preferencias= this.context.getSharedPreferences("Mis preferencias", Context.MODE_PRIVATE);
        String resultado = preferencias.getString(nombreDato,"");
        return resultado;
    }
}
