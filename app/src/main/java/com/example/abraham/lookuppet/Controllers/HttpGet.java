package com.example.abraham.lookuppet.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.HttpURLConnection;;
import java.net.URL;
import java.util.Scanner;

/**
 * @author: Abraham Cerda
 * Clase que se utiliza para realizar peticiones HTTP mediante el método GET
 */
public class HttpGet extends AsyncTask<String, Void, String> {

    private Context context;

    /**
     * Constructor
     */
    public HttpGet(Context context) {
        this.context = context;
    }// HttpGet(Context context)

    /**
     * Método que realiza la petición al servidor
     */
    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            return new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A").next();
        }
        catch(Exception e){
            Toast.makeText(context, "Error desconocido", Toast.LENGTH_LONG).show();
        }
        /*
        }catch (MalformedURLException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } catch (ProtocolException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        } catch (IOException e) {
            Log.e("ERROR", this.getClass().toString() + " " + e.toString());
        }*/
        return null;
    }// doInBackground(String... urls)

    /**
     * Método que manipula la respuesta del servidor
     */
    @Override
    protected void onPostExecute(String result) {
        Intent intent = new Intent("httpData").putExtra("data", result);
        context.sendBroadcast(intent);
    }// onPostExecute(String result)

}// HttpGet extends AsyncTask<String, Void, String>