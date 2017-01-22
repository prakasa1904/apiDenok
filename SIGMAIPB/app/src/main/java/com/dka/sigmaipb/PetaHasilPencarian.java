package com.dka.sigmaipb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dka.sigmaipb.peta.MyMarker;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PetaHasilPencarian extends AppCompatActivity {
    String key, val = null;
    JSONArray str_json = null;

    ImageView markerIcon = null;
    TextView markerLabel, anotherLabel, tahunLabel, wingLabel;
    View vmap;

    private GoogleMap mMap;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_hasil_pencarian);

        vmap = getLayoutInflater().inflate(R.layout.infomap, null);

        markerIcon = (ImageView) vmap.findViewById(R.id.marker_icon);

        markerLabel = (TextView) vmap.findViewById(R.id.marker_label);

        anotherLabel = (TextView) vmap.findViewById(R.id.another_label);

        tahunLabel = (TextView) vmap.findViewById(R.id.tahun);

        wingLabel = (TextView) vmap.findViewById(R.id.wing);

        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap = new HashMap<>();

        key = getIntent().getStringExtra("key").toLowerCase().replace(" ", "_");
        val = getIntent().getStringExtra("text");
        new GetDataMap().execute("http://172.20.10.4/SigmaIpb/api/get_asset/1/10/" + key + "/" + val);

        this.addListenerOnButton();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void addListenerOnButton() {
        FloatingActionButton button1 = (FloatingActionButton) findViewById(R.id.toEdite);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String sentKey, sentVal;
                sentKey = key;
                sentVal = val;
                Intent myIntent = new Intent(view.getContext(), Hasilpencarian.class);
                myIntent.putExtra("key", sentKey);
                myIntent.putExtra("text", sentVal);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PetaHasilPencarian Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.dka.sigmaipb/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "PetaHasilPencarian Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.dka.sigmaipb/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class GetDataMap extends AsyncTask<String, Void, Boolean> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(PetaHasilPencarian.this, "Mohon tunggu...", null, true, true);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String uri = params[0];

            BufferedReader bufferedReader;
            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json);
                }
                json = sb.toString().trim();
                JSONObject jObj = new JSONObject(json);
                str_json = jObj.getJSONArray("data");
                con.disconnect();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else {
                for (int i = 0; i < str_json.length(); i++) {
                    JSONObject ar = null;
                    try {
                        ar = str_json.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (ar != null) {
                            if (ar.getString("koordinat").length() > 0) {
                                /* Koordinate Creator */
                                String[] koordinat;
                                String LongLat = null;
                                try {
                                    LongLat = ar.getString("koordinat");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String latitude = "";
                                String longitude = "";
                                if (LongLat != null) {
                                    koordinat = LongLat.split(",");
                                    if (koordinat.length > 1) {
                                        if (koordinat[0] != null)
                                            latitude = koordinat[0].replace("(", "");

                                        if (koordinat[1] != null)
                                            longitude = koordinat[1].replace(")", "");
                                    }
                                }
                                /* END:: Koordinate Creator */
                                if (latitude.length() > 0 && longitude.length() > 0) {
                                    Log.e("Ada Data ", latitude + " === " + longitude);
                                    mMyMarkersArray.add(new MyMarker(PetaHasilPencarian.this, markerIcon, ar.getString("nama_barang"), ar.getString("kode_lokasi"), ar.getString("foto"), Double.parseDouble(longitude), Double.parseDouble(latitude), ar.getString("tahun"), ar.getString("wing")));
                                } else {
                                    Log.e("Tidak Ada Data ", " Long Lat");
                                }
                            } else {
                                Log.e("Tidak Ada Data ", " Long Lat");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setUpMap();
                plotMarkers(mMyMarkersArray);
                loading.dismiss();
                loading = null;
            }
        }
    }

    private void plotMarkers(ArrayList<MyMarker> markers) {
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));

                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            LatLngBounds IPB = new LatLngBounds(new LatLng(-6.558405, 106.725864), new LatLng(-6.557413, 106.733277));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(IPB.getCenter(), 17));

            // Check if we were successful in obtaining the map.

            if (mMap != null) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        marker.showInfoWindow();
                        return true;
                    }
                });
            } else
                Toast.makeText(getApplicationContext(), "Unable to create Maps", Toast.LENGTH_SHORT).show();
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            MyMarker myMarker = mMarkersHashMap.get(marker);


            if (myMarker.getmIcon().length() > 0) {
                myMarker.setBitmap();
                markerIcon.setImageBitmap(myMarker.getBitmap());
            } else {
                markerIcon.setImageResource(R.drawable.img_default);
            }
            markerLabel.setText(myMarker.getmLabel());
            anotherLabel.setText("Lokasi : " + myMarker.getmLokasi());
            tahunLabel.setText("Tahun : " + myMarker.getTahun()); // test error
            wingLabel.setText("Wing : " + myMarker.getWing());

            return vmap;
        }
    }
}