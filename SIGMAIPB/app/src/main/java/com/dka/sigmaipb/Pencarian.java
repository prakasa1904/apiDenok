package com.dka.sigmaipb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Pencarian extends AppCompatActivity{
    static String in_nama = "nama_barang";
    static String in_merk = "merk_type";
    JSONArray str_json = null;
    public ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
    private static final String JSON_URL = "http://172.20.10.4/SigmaIpb/api/get_asset/1/10/";

    public List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.get_data_list(JSON_URL);

        this.addListenerOnButton();

        /* Proses pencarian */
        Spinner sp = (Spinner) findViewById(R.id.pencarian);
        list.add("Nama Barang");
        list.add("Merk Barang");
        list.add("Tahun");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

    }

    public void addListenerOnButton() {

        Button button1 = (Button) findViewById(R.id.btnCari);
        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Spinner spinner = (Spinner)findViewById(R.id.pencarian);
                String key = spinner.getSelectedItem().toString();

                EditText editText = (EditText)findViewById(R.id.cari);
                String text = editText.getText().toString();

                Intent myIntent = new Intent(view.getContext(),Hasilpencarian.class);
                myIntent.putExtra("key",key);
                myIntent.putExtra("text",text);
                startActivity(myIntent);

            }
        });
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

    private void get_data_list(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Pencarian.this, "Mohon tunggu...",null,true,true);
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
                    str_json = jObj.getJSONArray("data");
                    for(int i = 0; i < str_json.length(); i++){
                        JSONObject ar = str_json.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String nama = ar.getString("nama_barang");
                        String merk = ar.getString("merk_type");
                        map.put(in_nama, nama);
                        map.put(in_merk, merk);
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
                loading.dismiss(); loading = null;
                ListView lv = (ListView) findViewById(R.id.list_view);

                String[] from = { in_nama, in_merk };
                int[] to = { R.id.nama_barang, R.id.merk_barang };
                ListAdapter adapter = new SimpleAdapter(getApplicationContext(), data_map,
                        R.layout.content_pencarian, from, to);
                lv.setAdapter(adapter);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
