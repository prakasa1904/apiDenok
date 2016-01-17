package com.dka.sigmaipb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dka.sigmaipb.cache.FileCache;
import com.dka.sigmaipb.cache.MemoryCache;
import com.dka.sigmaipb.cache.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Ubah extends AppCompatActivity {

    // Activity request codes
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public String imagepath = null;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "SIGMA IPB";

    private Uri fileUri; // file url to store image/video
    private static final String GET_URL = "http://172.20.10.4/SigmaIpb/api/get_lokasi";
    private static final String JSON_URL = "http://172.20.10.4/SigmaIpb/api/edit";
    public ArrayList<String> dataForm = new ArrayList<String>();

    private TextView messageText;
    public ImageView imageview;
    private Button btnAmbilGambar;
    private Button saveData;

    // Post data
    private TextView idData;
    private EditText kodeData, namaData, merkData, tahunData, hargaData, sumberData, latData, longData;
    private Spinner ruangData;
    public String spinnerSelected = null;
    private long fileSize = 0;

    Exif exif;

    /* Key Data From Other Activity */
    private String key, val, offset = null;

    /* Data Spinner */
    JSONArray str_json = null;
    public List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imgPreview);

        btnAmbilGambar = (Button) findViewById(R.id.btnAmbilGambar);
        saveData = (Button) findViewById(R.id.saveData);

        idData = (TextView)findViewById(R.id.id_barang);
        kodeData = (EditText)findViewById(R.id.kode_barang);
        namaData = (EditText) findViewById(R.id.nama_barang);
        merkData = (EditText) findViewById(R.id.merek_barang);
        tahunData = (EditText) findViewById(R.id.tb_barang);
        hargaData = (EditText) findViewById(R.id.harga_satuan_barang);
        sumberData = (EditText) findViewById(R.id.sumber_dana);
        ruangData = (Spinner) findViewById(R.id.ruangan);
        latData = (EditText) findViewById(R.id.latitude);
        longData = (EditText) findViewById(R.id.longitude);

        //textLon.setEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        offset = getIntent().getStringExtra("offset");
        key = getIntent().getStringExtra("key");
        val = getIntent().getStringExtra("text");
        if(key == null && val == null)
            new setDataForm().execute("http://172.20.10.4/SigmaIpb/api/get_asset/" + offset +"/1/");
        else
            new setDataForm().execute("http://172.20.10.4/SigmaIpb/api/get_asset/" + offset + "/1/" + key + "/" + val);

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Ubah.this);
                        alertDialogBuilder.setTitle("Warning !");
                        alertDialogBuilder.setMessage("Anda yakin data sudah benar ?");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        /* Data Builder Array List */
                                        String valueIds = idData.getText().toString();
                                        dataForm.add(valueIds);
                                        String valueId = kodeData.getText().toString();
                                        dataForm.add(valueId);
                                        String valueNama = namaData.getText().toString();
                                        dataForm.add(valueNama);
                                        String valueMerk = merkData.getText().toString();
                                        dataForm.add(valueMerk);
                                        String valueTahun = tahunData.getText().toString();
                                        dataForm.add(valueTahun);
                                        String valueHarga = hargaData.getText().toString();
                                        dataForm.add(valueHarga);
                                        String valueSumber = sumberData.getText().toString();
                                        dataForm.add(valueSumber);
                                        String valueRuang = ruangData.getSelectedItem().toString();
                                        dataForm.add(valueRuang);
                                        String valueLat = latData.getText().toString();
                                        dataForm.add(valueLat);
                                        String valueLong = longData.getText().toString();
                                        dataForm.add(valueLong);

                                        /* After Submit */
                                        dialog = ProgressDialog.show(Ubah.this, "", "Uploading file...", true);
                                        //messageText.setText("uploading started.....");
                                        new Thread(new Runnable() {
                                            public void run() {
                                                if(imagepath == null) imagepath = "false";
                                                uploadFileToServer(imagepath, JSON_URL, dataForm);
                                            }
                                        }).start();
                                        finish();
                                    }
                                });
                        alertDialogBuilder.create().show();
                    }
                });
    }

    class setDataForm extends AsyncTask<String, Void, Boolean>{
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Ubah.this, "Mohon tunggu...",null,true,true);
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
                con.disconnect();
                return true;
            }catch(Exception ex){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            else
                for(int i = 0; i < str_json.length(); i++){
                    JSONObject ar = null;
                    try {
                        ar = str_json.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /* SPINNER DATA SELECTED */
                    try {
                        spinnerSelected = ar.getString("kode_lokasi");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /* Koordinate Creator */
                    String[] koordinat = null;
                    String LongLat = "";
                    try {
                        LongLat = ar.getString("koordinat");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String latitude = ""; String longitude = "";
                    if( LongLat != "" ){
                        koordinat = LongLat.split(",");
                        if(koordinat.length > 1) {
                            if ( koordinat[0] != null )
                                latitude = koordinat[0].replace("(", "");

                            if ( koordinat[1] != null )
                                longitude = koordinat[1].replace(")", "");
                        }
                    }
                    /* END:: Koordinate Creator */
                    try {
                        idData.setText(ar.getString("id_barang"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        kodeData.setText(ar.getString("kode_barang"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        namaData.setText(ar.getString("nama_barang"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        merkData.setText(ar.getString("merk_type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        tahunData.setText(ar.getString("tahun"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        hargaData.setText(ar.getString("harga_satuan"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        sumberData.setText(ar.getString("sumber_dana"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    latData.setText(latitude);
                    longData.setText(longitude);

                    try {
                        new DownloadImageTask(imageview).execute(ar.getString("foto"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            new GetListLokasi(spinnerSelected).execute(GET_URL);
            loading.dismiss(); loading = null;
        }

        class GetListLokasi extends AsyncTask<String, Void, String> {
            private String strValue = null;

            public GetListLokasi (String str0) {
                this.strValue = str0;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
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
                    str_json = jObj.getJSONArray("lokasi");
                    for(int i = 0; i < str_json.length(); i++){
                        JSONObject ar = str_json.getJSONObject(i);
                        String id = ar.getString("kode");
                        list.add(id);
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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Ubah.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ruangData.setAdapter(dataAdapter);

                if ( ! this.strValue.equals(null) ){
                    ruangData.setSelection(getIndex(ruangData, this.strValue));
                }
            }

            private int getIndex(Spinner spinner,String value){
                int index = 0;
                for (int i = 0; i < spinner.getAdapter().getCount(); i++){
                    String str1 = (String) spinner.getItemAtPosition(i);
                    if( str1.toLowerCase().contains(value.toLowerCase()) ){
                        index = i;
                    }
                }
                return index;
            }
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            FileCache fileCache;
            MemoryCache memoryCache = new MemoryCache();

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
                this.fileCache = new FileCache(Ubah.this);
            }

            protected Bitmap doInBackground(String... urls) {
            /* Define URL */
                String urldisplay = urls[0];

                if( urldisplay.length() < 1 ){
                    return null;
                }else{
                /* Check URL in file exist or NOT ! */
                    File f = this.fileCache.getFile(urldisplay);

                    Bitmap b = decodeFile(f);
                    if(b != null) return b;
                    else {
                        try {
                            Bitmap bitmap = null;
                            URL uri = new URL(urldisplay);
                            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                            conn.setConnectTimeout(30000);
                            conn.setReadTimeout(30000);
                            conn.setInstanceFollowRedirects(true);

                        /* Check URL OK ? */
                            int status = conn.getResponseCode();
                            if(status == 200){
                                InputStream is = conn.getInputStream();
                                OutputStream os = new FileOutputStream(f);

                            /* Write Bitmap and URL to Cache */
                                Utils.CopyStream(is, os);

                                bitmap = decodeFile(f);
                                os.close();
                                conn.disconnect();

                                memoryCache.put(urldisplay, bitmap);

                                return bitmap;
                            }else{
                                return null;
                            }
                        } catch (Exception e) {
                            Log.e("Error Sigma Man ", e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    }
                }
            }

            protected void onPostExecute(Bitmap result) {
                if( result != null ) this.bmImage.setImageBitmap(result);
                else this.bmImage.setImageResource(R.drawable.img_default);
            }

            private Bitmap decodeFile(File f){
                try {
                    //Decode image size
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    FileInputStream stream1=new FileInputStream(f);
                    BitmapFactory.decodeStream(stream1,null,o);
                    stream1.close();

                    //Find the correct scale value. It should be the power of 2.

                    // Set width/height of recreated image
                    final int REQUIRED_SIZE=85;

                    int width_tmp=o.outWidth, height_tmp=o.outHeight;
                    int scale=1;
                    while(true){
                        if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                            break;
                        width_tmp/=2;
                        height_tmp/=2;
                        scale*=2;
                    }

                    //decode with current scale values
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize=scale;
                    FileInputStream stream2=new FileInputStream(f);
                    Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
                    stream2.close();
                    return bitmap;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }

    // Checking device has camera hardware or not
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    // Here we store the file url as it will be null after returning from camera app
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    // Receiving activity result method will be called after closing the camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        if (resultCode == Activity.RESULT_OK) {
            Uri LatLong = data.getData();
            //Uri Long = data.getData();

            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

            /*exif = new Exif(LatLong, this);
            textLat.setText(exif.getLat());
            textLon.setText(exif.getLon());*/

            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled()){
                String stringLatitude = String.valueOf(gpsTracker.latitude);
                latData.setText(stringLatitude);

                String stringLongitude = String.valueOf(gpsTracker.longitude);
                longData.setText(stringLongitude);
            }
        }
    }

    /* Galleries Method */

    private void selectImage() {
        final CharSequence[] items = { "Kamera", "Galeri", "Kembali" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Ubah.this);
        builder.setTitle("Tambah Foto!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Kamera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Galeri")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Kembali")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);
        imagepath = selectedImagePath;

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 300;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        imageview.setImageBitmap(bm);
        messageText.setText("Sukses");
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageview.setImageBitmap(thumbnail);
    }

    public static String uploadFileToServer(String filename, String targetUrl, ArrayList<String> dataForm) {
        String response = "error";
        FileInputStream fileInputStream = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024;

        try {
            if(filename != "false")
                fileInputStream = new FileInputStream(new File(filename));

            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(1024);
            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);

            ArrayList<String> keyForm = new ArrayList<String>();
            keyForm.add("id");
            keyForm.add("kode_barang");
            keyForm.add("nama_barang");
            keyForm.add("merk_type");
            keyForm.add("tahun");
            keyForm.add("harga_satuan");
            keyForm.add("sumber_dana");
            keyForm.add("kode_lokasi");
            keyForm.add("latitude");
            keyForm.add("longitude");
            for (int i = 0; i < keyForm.size(); i++) {
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + keyForm.get(i) + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
                outputStream.writeBytes("Content-Length: " + dataForm.get(i).length() + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(dataForm.get(i) + lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                ;
            }

            if (fileInputStream != null) {
                String connstr;
                connstr = "Content-Disposition: form-data; name=\"UploadFile\";filename=\""
                        + filename + "\"" + lineEnd;

                outputStream.writeBytes(connstr);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                System.out.println("Image length " + bytesAvailable + "");
                try {
                    while (bytesRead > 0) {
                        try {
                            outputStream.write(buffer, 0, bufferSize);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            response = "outofmemoryerror";
                            return response;
                        }
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                    return response;
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);
            }
            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();

            if (serverResponseCode == 200) response = "true";
            else response = "false";

            fileInputStream.close(); outputStream.flush();

            connection.getInputStream(); java.io.InputStream is = connection.getInputStream();

            int ch;
            StringBuffer b = new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }

            outputStream.close();
            outputStream = null;

        } catch (Exception ex) {
            // Exception handling
            response = "error";
            ex.printStackTrace();
            Log.e("Ini Lhoh Error ", ex.getMessage());
        }
        return response;
    }
}
