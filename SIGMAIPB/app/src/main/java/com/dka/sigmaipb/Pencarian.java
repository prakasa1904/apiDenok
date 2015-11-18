package com.dka.sigmaipb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

// Luar Libs
import org.json.JSONObject;
import org.json.JSONException;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dka.sigmaipb.KoneksiDB;


public class Pencarian extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView list = (TextView) findViewById(R.id.view_list);
        setSupportActionBar(toolbar);

        KoneksiDB connection = new KoneksiDB();
        try {
            list.setText(connection.getKoneksi());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // Untuk panggil Data dari URL
    protected void jSOnObject(){
        URL url = null;
        try {
            url = new URL("http://172.20.10.4/SigmaIpb/api/list_lokasi?limit=5");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            if (urlConnection == null) throw new AssertionError();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(200);
            urlConnection.setReadTimeout(200);
            urlConnection.connect();

            int status = urlConnection.getResponseCode();
            if(status == 201 || status == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    sb.append(line + '\n');
                }
                br.close();
                String dataJson = sb.toString();
                //JSONObject jObj = new JSONObject(dataJson);
            }

        } catch(MalformedURLException ex){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            urlConnection.disconnect();
        }
    }

}
