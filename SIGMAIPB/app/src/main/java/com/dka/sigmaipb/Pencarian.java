package com.dka.sigmaipb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dka.sigmaipb.jsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Pencarian extends AppCompatActivity {
    static String in_judul = "judul";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TextView list = (TextView) findViewById(R.id.view_list);
        setSupportActionBar(toolbar);
        
        // Data Json Here
        //jsonParser data = new jsonParser();
        //if(data.dataList() != null) {
            //list.setText((CharSequence) data.dataList());
        //}else {
            //list.setText(null);
        //}

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tanpa Aksi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    // Untuk panggil Data dari URL
    private class listLokasi extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(Pencarian.this);
            pDialog.setMessage("Sabar gan, masih ngambil data neh...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args){
            jsonParser source = new jsonParser();
            JSONObject data = source.dataListLokasi();

            try{
                JSONArray str_json = data.getJSONArray("berita");
                for(int i = 0; i < str_json.length(); i++){
                    JSONObject ar = str_json.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    String judul = ar.getString("title");

                    map.put(in_judul, judul);
                    data_map.add(map);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public ListAdapter listAdapter;

                public void setListAdapter(ListAdapter listAdapter) {
                    this.listAdapter = listAdapter;
                }

                public void run() {
                    ListAdapter adapter = new SimpleAdapter(Pencarian.this, data_map,R.layout.activity_pencarian, new String[] { in_judul });
                    setListAdapter(adapter);
                }
            }
        }
    }
}
