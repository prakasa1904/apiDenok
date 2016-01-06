package com.dka.sigmaipb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Hasilpencarian extends AppCompatActivity{
    private static String FINAL_URL = "";
    static String in_nama = "nama_barang";
    static String in_merk = "merk_type";
    JSONArray str_json = null;
    public ArrayList<HashMap<String, String>> data_map = new ArrayList<HashMap<String, String>>();
    private static final String JSON_URL = "http://172.20.10.4/SigmaIpb/api/get_asset/1/10/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_pencarian);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /* Create URL Search */
        String key = getIntent().getStringExtra("key").toLowerCase().replace(" ", "_");
        FINAL_URL = JSON_URL + key + "/" + getIntent().getStringExtra("text");
        this.get_data_list(FINAL_URL); // final
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
                loading = ProgressDialog.show(Hasilpencarian.this, "Mohon tunggu...",null,true,true);
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
                        R.layout.content_hasil_pencarian, from, to);
                lv.setAdapter(adapter);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
