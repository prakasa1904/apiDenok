package com.dka.sigmaipb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class Beranda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        Button tambah = (Button) findViewById(R.id.btnTambah);
        Button pencarian = (Button) findViewById(R.id.btnPencarian);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tambah.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent i = new Intent(Beranda.this, Tambah.class);
                //i.putExtra("pesan", "From Activity Beranda");
                //startActivity(i);
                SharedPreferences prefs = getSharedPreferences("IPB", 0);
                boolean hasCode = prefs.getBoolean("HAS_CODE", false);
                Context context = Beranda.this;
                Intent intent;

                if(!hasCode){
                    intent = new Intent(context, Tambah.class);
                }else{
                    intent = new Intent(context, Tambah.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                (context).startActivity(intent);
            }
        });

        pencarian.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Beranda.this, Pencarian.class);
                i.putExtra("pesan", "From Activity Beranda");
                i.putExtra(Settings.EXTRA_AUTHORITIES, new String[]{"com.dka.sigmaipb"});
                startActivity(i);
                //startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
                //SharedPreferences prefs = getSharedPreferences("IPB", 0);
                //boolean hasCode = prefs.getBoolean("HAS_CODE", false);
                //Context context = Beranda.this;
                //Intent intent = null;

                //if(!hasCode){
                    //intent = new Intent(getApplicationContext(), Pencarian.class);
                //}else{
                    //intent = new Intent(getApplicationContext(), Pencarian.class);
                //}
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //(context).startActivity(intent);
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
        }
