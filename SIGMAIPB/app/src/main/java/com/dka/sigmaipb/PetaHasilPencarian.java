package com.dka.sigmaipb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class PetaHasilPencarian extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_hasil_pencarian);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent i = new Intent(PetaHasilPencarian.this, Hasilpencarian.class);
                i.putExtra("pesan", "From Activity Main");
                startActivity(i);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Create a LatLngBounds.
        LatLngBounds IPB = new LatLngBounds(new LatLng(-6.558405, 106.725864), new LatLng(-6.557413, 106.733277));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(IPB.getCenter(), 17));
    }
}