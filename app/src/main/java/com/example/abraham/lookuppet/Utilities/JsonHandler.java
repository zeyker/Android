package com.example.abraham.lookuppet.Utilities;

import android.app.ExpandableListActivity;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by abraham on 16-01-2016.
 */
public class JsonHandler {

    public String postUser(String nombreReal, String nombreUser, String correo, String contra) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("idUsuario",0);
            jsonObject.accumulate("activoUsuario", 1);
            jsonObject.accumulate("contrasena", contra);
            jsonObject.accumulate("correo", correo);
            jsonObject.accumulate("ipUltimoLogeo", "127.0.0.1");
            jsonObject.accumulate("nombreUsuario", nombreUser);
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            jsonObject.accumulate("timestampUltimoLogeo", today);
            jsonObject.accumulate("tipoUsuario", 0);
            return jsonObject.toString();
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }

    public String postPerdida(String tipo, String comuna, String correoContacto, String fonoContacto, String descripcion){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("idPublicacion",0);
            jsonObject.accumulate("activoPublicacion", 1);
            jsonObject.accumulate("descripcion", descripcion);
            jsonObject.accumulate("email", correoContacto);
            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            jsonObject.accumulate("fechaPublicacion", today);
            jsonObject.accumulate("fonoContacto", fonoContacto);
            jsonObject.accumulate("latitud", 0);
            jsonObject.accumulate("longitud",0);
            jsonObject.accumulate("titulo", "Este es mi titulo");
            return jsonObject.toString();
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }

    public String putEditar(String nombreReal, String nombreUser, String descripcion){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("hola", nombreReal);
        } catch (JSONException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }

    public String postLogeo(String nombreUser, String contra){
        try{
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("nombreUsuario",nombreUser);
            jsonObject.accumulate("contrasena",contra);
            return jsonObject.toString();
        }
        catch(Exception e){
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }

    public  String getUser(String user){
        try{
        JSONObject jsonObject = new JSONObject(user);
            return jsonObject.getString("idUsuario");
    }
        catch(Exception e){
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }

    public String[] getPerfilUser(String user) {
        try {
            String resultado[]= new String[3];
            JSONObject jsonObject = new JSONObject(user);
            resultado[0]= jsonObject.getString("nombreReal");
            resultado[1]=jsonObject.getString("nombreUsuario");
            resultado[2]= jsonObject.getString("descripcion");
            return resultado;
        }
        catch(Exception e){
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }
        return null;
    }
}
