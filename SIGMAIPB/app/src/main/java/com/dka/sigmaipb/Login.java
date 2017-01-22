package com.dka.sigmaipb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

// Created by prakasa on 13/10/16.
public class Login extends AppCompatActivity {

    EditText ed1,ed2;
    Button b1;
    Intent in;

    String pesan = null;

    private static final String JSON_URL = "http://172.20.10.4/SigmaIpb/api/get_user_id";
    public ArrayList<String> dataForm = new ArrayList<>();

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Userid = "0";
    public static final String Username = "nameKey";
    public static final String Password = "phoneKey";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed1=(EditText)findViewById(R.id.username);
        ed2=(EditText)findViewById(R.id.password);

        b1=(Button)findViewById(R.id.loginAja);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        pesan = getIntent().getStringExtra("pesan");
        if( pesan != null ) {
            Toast.makeText(getApplicationContext(), "Maaf Username Password Salah", Toast.LENGTH_LONG).show();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uname  = ed1.getText().toString();
                dataForm.add(uname);
                final String passw  = ed2.getText().toString();
                dataForm.add(passw);

                new Thread(new Runnable() {
                    public void run() {
                        String serverId = uploadFileToServer(JSON_URL, dataForm);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        if( parseInt(serverId) > 0 ){
                            editor.putString(Userid, serverId);
                            editor.putString(Username, uname);
                            editor.putString(Password, passw);
                            editor.apply();

                            Context context = Login.this;
                            Intent intent;
                            intent = new Intent(context, Beranda.class);
                            (context).startActivity(intent);
                        }else{
                            Context context = Login.this;
                            Intent intent;
                            intent = new Intent(context, Login.class);
                            intent.putExtra("pesan", "Maaf Username Password Salah");
                            (context).startActivity(intent);
                        }
                    }
                }).start();
                finish();
            }
        });
    }

    public static String uploadFileToServer(String targetUrl, ArrayList<String> dataForm) {
        String content; String result;
        StringBuilder sb = new StringBuilder();

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Responses from the server (code and message)

                // Allow Inputs & Outputs
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setChunkedStreamingMode(1024);
                // Enable POST method
                connection.setRequestMethod("POST");

                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);

                ArrayList<String> keyForm = new ArrayList<>();
                keyForm.add("username");
                keyForm.add("password");
                for (int i = 0; i < keyForm.size(); i++) {
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + keyForm.get(i) + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
                    outputStream.writeBytes("Content-Length: " + dataForm.get(i).length() + lineEnd);
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(dataForm.get(i) + lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                }

            int serverResponseCode = connection.getResponseCode();
            if (serverResponseCode == 200) {


                BufferedInputStream output = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(output));
                while ((content = br.readLine()) != null) {
                    sb.append(content);
                }
                result = sb.toString();

                outputStream.flush();
                outputStream.close();
            }else{
                result = "0";
            }
        } catch (Exception ex) {
            result = "0";
            ex.printStackTrace();
            Log.e("e", ex.getMessage());
        }

        return result;
    }
}
