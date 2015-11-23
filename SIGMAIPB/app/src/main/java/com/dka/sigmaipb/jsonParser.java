package com.dka.sigmaipb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by prakasa on 20/11/15.
 */
public class jsonParser {
    public JSONObject dataListLokasi(){
        URL url = null;
        try {
            url = new URL("http://192.168.43.173");
        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException ex){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (urlConnection == null) throw new AssertionError();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setRequestProperty("Content-length", "0");
            //urlConnection.setUseCaches(false);
            //urlConnection.setAllowUserInteraction(false);
            //urlConnection.setConnectTimeout(200);
            //urlConnection.setReadTimeout(200);
            urlConnection.connect();

            //int status = urlConnection.getResponseCode();
            //if (status == 201 || status == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + '\n');
                }
                String dataJson = sb.toString();
                JSONObject jObj = new JSONObject(dataJson);
                br.close();
                return jObj;
            //} else {
                //return null;
            //}

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
