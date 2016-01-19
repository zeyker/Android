package com.example.abraham.lookuppet.Controllers;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author:Abraham Cerda
 */
public class HttpPost extends AsyncTask<String, Void, String> {
    private Context context;
    private String objetoJson;

    public HttpPost(Context context, String objetoJson){
        this.context=context;
        this.objetoJson=objetoJson;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(objetoJson.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.connect();
            OutputStream os = new BufferedOutputStream(connection.getOutputStream());
            os.write(objetoJson.getBytes());
            os.flush();
            String realizado="realizado";
            return realizado;
        }
        catch(Exception e){
            Looper.prepare();
            Toast.makeText(context, "Error desconocido", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Intent intent = new Intent("HttpPost").putExtra("postData", result);
        context.sendBroadcast(intent);
    }// onPostExecute(String result)

}// HttpPost