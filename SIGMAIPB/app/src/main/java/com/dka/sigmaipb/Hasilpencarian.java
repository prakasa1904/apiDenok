package com.dka.sigmaipb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Hasilpencarian extends AppCompatActivity{
    String key, val = "";
    Integer pagination = 1;
    static String in_nama = "nama_barang";
    static String in_merk = "merk_type";
    static String in_foto = "foto";
    JSONArray str_json = null;

    ArrayList<Data>  dataList = new ArrayList<Data>();
    DataAdapter adapter; ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_pencarian);
        key = getIntent().getStringExtra("key").toLowerCase().replace(" ", "_");
        val = getIntent().getStringExtra("text");
        new getJSON().execute("http://172.20.10.4/SigmaIpb/api/get_asset/1/10/" + key + "/" + val);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.list_view);
        /*adapter = new DataAdapter(getApplicationContext(), R.layout.content_hasil_pencarian, dataList);
        listview.setAdapter(adapter);*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), Ubah.class);
                String offset = String.valueOf(position + 1);
                myIntent.putExtra("offset", offset);
                myIntent.putExtra("key", key);
                myIntent.putExtra("text", val);
                startActivity(myIntent);
            }
        });

        this.addListenerOnButton();
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

    public void addListenerOnButton() {
        Button button1 = (Button) findViewById(R.id.btnNext);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pagination += 1;
                new getJSON().execute("http://172.20.10.4/SigmaIpb/api/get_asset/" + pagination + "/10/" + key + "/" + val);
                /*runOnUiThread(new Runnable() {
                    public void run() {

                        listview.setAdapter(adapter);

                    }
                });*/

            }
        });
    }

    class getJSON extends AsyncTask<String, Void, Boolean>{
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Hasilpencarian.this, "Mohon tunggu...",null,true,true);
        }

        @Override
        protected Boolean doInBackground(String... params) {

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
                str_json = jObj.getJSONArray("data");
                for(int i = 0; i < str_json.length(); i++){
                    JSONObject ar = str_json.getJSONObject(i);
                    Data data = new Data();
                    data.setName(ar.getString(in_nama));
                    data.setMerk(ar.getString(in_merk));
                    data.setImage(ar.getString(in_foto));
                    dataList.add(data);
                }
                con.disconnect();
                return true;
            }catch(Exception ex){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            loading.dismiss(); loading = null;
            runOnUiThread(new Runnable() {
                public void run() {
                    adapter = new DataAdapter(getApplicationContext(), R.layout.content_hasil_pencarian, dataList);
                    adapter.notifyDataSetChanged();
                    listview.setAdapter(adapter);
                }
            });

            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}
