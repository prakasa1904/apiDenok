package com.dka.sigmaipb;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pencarian extends ListActivity{
    static String in_judul = "judul";
    static String in_penulis = "penulis";
    JSONArray str_json = null;
    ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
    private static final String JSON_URL = "http://192.168.43.173/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatActivity acBar = new AppCompatActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Data Json
        //textViewJSON = (TextView) findViewById(R.id.textViewJSON);
        //textViewJSON.setMovementMethod(new ScrollingMovementMethod());
        //ListAdapter adapter = new SimpleAdapter(Pencarian.this, data_map,
                //R.layout.content_pencarian, new String[]{in_judul, in_penulis},
                //new int[]{R.id.judul, R.id.penulis});

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Tanpa Aksi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.getJSON(JSON_URL);
        this.listView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beranda, menu);
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

    public void listView() {
        ListAdapter adapter = new SimpleAdapter(Pencarian.this, data_map,
                R.layout.content_pencarian, new String[]{in_judul, in_penulis},
                new int[]{R.id.judul, R.id.penulis});
        setListAdapter(adapter);
        //ListView lv = getListView();
        //lv.setOnItemClickListener(new OnItemClickListener() {

            //@Override
        //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //String kode = ((TextView) view.findViewById(R.id.kode)).getText().toString();

        //Intent in = new Intent(AksesServerActivity.this, DetailAksesServer.class);
        //in.putExtra(AR_ID, kode);
        //startActivity(in);

        //}
        //});
    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Pencarian.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json = "";
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json);
                    }
                    json = sb.toString().trim();
                    JSONObject jObj = new JSONObject(json);
                    str_json = jObj.getJSONArray("berita");
                    for(int i = 0; i < str_json.length(); i++){
                        JSONObject ar = str_json.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String penulis = ar.getString("author");
                        String judul = ar.getString("title");
                        map.put(in_judul, judul);
                        map.put(in_penulis, penulis);
                        data_map.add(map);
                    }
                    con.disconnect();
                }catch(Exception ex){
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //textViewJSON.setText(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
