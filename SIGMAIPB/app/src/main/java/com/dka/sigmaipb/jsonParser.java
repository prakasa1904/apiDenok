package com.dka.sigmaipb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prakasa on 20/11/15.
 */
public class jsonParser {
    public ProgressDialog loading;
    public String URL_DATA;
    public Context outContext;
    public JSONArray str_json;

    ArrayList<HashMap<String, String>> data_ruang = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> data_aset = new ArrayList<HashMap<String, String>>();


    jsonParser(String Url, Context mcontext) {
        URL_DATA = Url;
        outContext = mcontext;
    }

/*    private ArrayList<HashMap<String, String>> json_lokasi() {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            private String in_id, in_nama;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mcontext, "Please Wait...", null, true, true);
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
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    json = sb.toString().trim();
                    JSONObject jObj = new JSONObject(json);
                    str_json = jObj.getJSONArray("lokasi");
                    for (int i = 0; i < str_json.length(); i++) {
                        JSONObject ar = str_json.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String id = ar.getString("kode_lokasi");
                        String nama = ar.getString("nama_ruang");
                        map.put(in_id, id);
                        map.put(in_nama, nama);
                        data_ruang.add(map);
                    }
                    con.disconnect();
                } catch (Exception ex) {
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
        gj.execute(Url);
        return data_ruang;
    }*/

    public ArrayList<HashMap<String, String>> json_asset() {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            private String id_barang = "id_barang",
                    kode_barang = "kode_barang",
                    nama_barang = "nama_barang",
                    merk_type = "merk_type",
                    tahun = "tahun",
                    harga_satuan = "harga_satuan",
                    sumber_dana = "sumber_dana",
                    kode_lokasi = "kode_lokasi",
                    longitude = "longitude",
                    latitude = "latitude",
                    koordinat = "koordinat",
                    foto = "foto";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(outContext, "Please Wait...", null, true, true);
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
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json);
                    }
                    json = sb.toString().trim();
                    JSONObject jObj = new JSONObject(json);
                    str_json = jObj.getJSONArray("lokasi");
                    for (int i = 0; i < str_json.length(); i++) {
                        JSONObject ar = str_json.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put( id_barang, ar.getString("id_barang") );
                        map.put(kode_barang, ar.getString("kode_barang") );
                        map.put(nama_barang, ar.getString("nama_barang") );
                        map.put(merk_type, ar.getString("merk_type") );
                        map.put(tahun, ar.getString("tahun") );
                        map.put(harga_satuan, ar.getString("harga_satuan") );
                        map.put(sumber_dana, ar.getString("sumber_dana") );
                        map.put(kode_lokasi, ar.getString("kode_lokasi") );
                        map.put(koordinat, ar.getString("koordinat") );
                        String[] new_kordinat = ar.getString("koordinat").split(",");
                        map.put(longitude, new_kordinat[0] );
                        map.put(latitude, new_kordinat[1] );
                        map.put(foto, ar.getString("foto") );
                        data_aset.add(map);
                    }
                    con.disconnect();
                } catch (Exception ex) {
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
        GetJSON json = new GetJSON();
        json.execute(URL_DATA);
        return data_aset;
    }
}